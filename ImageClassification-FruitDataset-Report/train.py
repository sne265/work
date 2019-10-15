#Program to train and classify images of fruits
from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
import argparse
import hashlib
import os.path
import random
import re
import struct
import sys
import tarfile
import numpy as np
from six.moves import urllib
import tensorflow as tf
from tensorflow.python.framework import graph_util
from tensorflow.python.framework import tensor_shape
from tensorflow.python.platform import gfile
from tensorflow.python.util import compat

FLAGS = None

#URL for downloadig the inception model
INCEPTION_URL = 'http://download.tensorflow.org/models/image/imagenet/inception-2015-12-05.tgz'

#Defining parameters that are used in this
#model architecture for Inception v3, like tensor names and their sizes.
BN_TENSOR_NAME = 'pool_3/_reshape:0'
BN_TENSOR_SIZE = 2048
MODEL_INPUT_WIDTH = 100
MODEL_INPUT_HEIGHT = 100
MODEL_INPUT_DEPTH = 3
JPEG_DATA_TENSOR_NAME = 'DecodeJpeg/contents:0'
RESIZED_INPUT_TENSOR_NAME = 'ResizeBilinear:0'
MAX_NUM_IMAGES_PER_CLASS = 2 ** 27 - 1  # ~134M

#To create a list of training images from the file system.
#this method splits the images into training,testing and validation sets based on the input arguments
#it returns a list of images for each label and their paths
def listing_images(image_dir, percentage_for_testing, percentage_for_validation):
    if not gfile.Exists(image_dir):
        print("Image directory '" + image_dir + "' not found.")
        return None
    result = {}
    sub_dirs = [x[0] for x in gfile.Walk(image_dir)]
    # The root directory comes first, so skip it.
    is_root_dir = True
    for sub_dir in sub_dirs:
        if is_root_dir:
            is_root_dir = False
            continue
        extensions = ['jpg', 'jpeg', 'JPG', 'JPEG']
        file_list = []
        dir_name = os.path.basename(sub_dir)
        if dir_name == image_dir:
            continue
        print("Looking for images in '" + dir_name + "'")
        for extension in extensions:
            file_glob = os.path.join(image_dir, dir_name, '*.' + extension)
            file_list.extend(gfile.Glob(file_glob))
        if not file_list:
            print('No files found')
            continue
        if len(file_list) < 20:
            print('WARNING: Folder has less than 20 images, which may cause issues.')
        elif len(file_list) > MAX_NUM_IMAGES_PER_CLASS:
            print('WARNING: Folder {} has more than {} images. Some images will '
                  'never be selected.'.format(dir_name, MAX_NUM_IMAGES_PER_CLASS))
        label_name = re.sub(r'[^a-z0-9]+', ' ', dir_name.lower())
        training_images = []
        testing_images = []
        validation_images = []
        for file_name in file_list:
            base_name = os.path.basename(file_name)
            hash_name = re.sub(r'_nohash_.*$', '', file_name)
            hash_name_hashed = hashlib.sha1(compat.as_bytes(hash_name)).hexdigest()
            percentage_hash = ((int(hash_name_hashed, 16) %
                                (MAX_NUM_IMAGES_PER_CLASS + 1)) *
                             (100.0 / MAX_NUM_IMAGES_PER_CLASS))
            if percentage_hash < percentage_for_validation:
                validation_images.append(base_name)
            elif percentage_hash < (percentage_for_testing + percentage_for_validation):
                testing_images.append(base_name)
            else:
                training_images.append(base_name)
        result[label_name] = {
            'dir': dir_name,
            'training': training_images,
            'testing': testing_images,
            'validation': validation_images,
            }
    return result

#To find a path to a particular bottleneck file
def discover_bottleneck_directory(image_lists, label_name, index, bottleneck_dir, category):
    return directory_for_images(image_lists, label_name, index, bottleneck_dir,
                        category) + '.txt'

#To find the path to an image for the label and the index specified in the argument list
def directory_for_images(image_lists, label_name, index, image_dir, category):
    if label_name not in image_lists:
        tf.logging.fatal('Label does not exist %s.', label_name)
    label_lists = image_lists[label_name]
    if category not in label_lists:
        tf.logging.fatal('Category does not exist %s.', category)
    category_list = label_lists[category]
    if not category_list:
        tf.logging.fatal('Label %s has no images in the category %s.', label_name, category)
    mod_index = index % len(category_list)
    base_name = category_list[mod_index]
    sub_dir = label_lists['dir']
    full_path = os.path.join(image_dir, sub_dir, base_name)
    return full_path

#Takes image data as input and returns an array of bottleneck values for that image
def bottleneck_execution(sess, image_data, image_data_tensor, bottleneck_tensor):
    bottleneck_values = sess.run(
        bottleneck_tensor,
        {image_data_tensor: image_data})
    bottleneck_values = np.squeeze(bottleneck_values)
    return bottleneck_values

#Creates a graph for inception using GraphDef
def graph_creation():
    with tf.Graph().as_default() as graph:
        model_filename = os.path.join(FLAGS.model_dir, 'classify_image_graph_def.pb')
        with gfile.FastGFile(model_filename, 'rb') as f:
            graph_def = tf.GraphDef()
            graph_def.ParseFromString(f.read())
            bottleneck_tensor, jpeg_data_tensor, resized_input_tensor = (
                tf.import_graph_def(graph_def, name='', return_elements=[
                    BN_TENSOR_NAME, JPEG_DATA_TENSOR_NAME,
                    RESIZED_INPUT_TENSOR_NAME]))
    return graph, bottleneck_tensor, jpeg_data_tensor, resized_input_tensor

#Check if a directory exists
def verify_directory(dir_name):
    if not os.path.exists(dir_name):
        os.makedirs(dir_name)

#Checks if files for inception model are present or not
def inception_model_files_check():
    dest_directory = FLAGS.model_dir
    if not os.path.exists(dest_directory):
        os.makedirs(dest_directory)
    filename = INCEPTION_URL.split('/')[-1]
    filepath = os.path.join(dest_directory, filename)
    if not os.path.exists(filepath):
        def _progress(count, block_size, total_size):
            sys.stdout.write('\r>> Downloading %s %.1f%%' %
                       (filename,
                        float(count * block_size) / float(total_size) * 100.0))
            sys.stdout.flush()
        filepath, _ = urllib.request.urlretrieve(INCEPTION_URL, filepath, _progress)
        print()
        statinfo = os.stat(filepath)
        print('Successfully downloaded', filename, statinfo.st_size, 'bytes.')
    tarfile.open(filepath, 'r:gz').extractall(dest_directory)

#To create a single file for bottleneck values
def bottleneckFiles_creation(bottleneck_path, image_lists, label_name, index,
                           image_dir, category, sess, jpeg_data_tensor,
                           bottleneck_tensor):
    image_path = directory_for_images(image_lists, label_name, index,
                              image_dir, category)
    if not gfile.Exists(image_path):
        tf.logging.fatal('File does not exist %s', image_path)
    image_data = gfile.FastGFile(image_path, 'rb').read()
    try:
        bottleneck_values = bottleneck_execution(
            sess, image_data, jpeg_data_tensor, bottleneck_tensor)
    except:
        raise RuntimeError('Error during processing file %s' % image_path)

    bottleneck_string = ','.join(str(x) for x in bottleneck_values)
    with open(bottleneck_path, 'w') as bottleneck_file:
        bottleneck_file.write(bottleneck_string)

#Fetches bottleneck file containing bottleneck values for an image
#if it does not exist then create a bottleneck file for that image
#returns bottleneck values of that particular image
def verify_bootleneck_existence(sess, image_lists, label_name, index, image_dir,
                             category, bottleneck_dir, jpeg_data_tensor,
                             bottleneck_tensor):
    label_lists = image_lists[label_name]
    sub_dir = label_lists['dir']
    sub_dir_path = os.path.join(bottleneck_dir, sub_dir)
    verify_directory(sub_dir_path)
    bottleneck_path = discover_bottleneck_directory(image_lists, label_name, index,
                                        bottleneck_dir, category)
    if not os.path.exists(bottleneck_path):
        bottleneckFiles_creation(bottleneck_path, image_lists, label_name, index,
                           image_dir, category, sess, jpeg_data_tensor,
                           bottleneck_tensor)
    with open(bottleneck_path, 'r') as bottleneck_file:
        bottleneck_string = bottleneck_file.read()
    did_hit_error = False
    try:
        bottleneck_values = [float(x) for x in bottleneck_string.split(',')]
    except ValueError:
        print('Invalid float found, recreating bottleneck')
        did_hit_error = True
    if did_hit_error:
        bottleneckFiles_creation(bottleneck_path, image_lists, label_name, index,
                           image_dir, category, sess, jpeg_data_tensor,
                           bottleneck_tensor)
        with open(bottleneck_path, 'r') as bottleneck_file:
            bottleneck_string = bottleneck_file.read()
        bottleneck_values = [float(x) for x in bottleneck_string.split(',')]
    return bottleneck_values


def random_retrieving_bottleneck_cache(sess, image_lists, how_many, category,
                                  bottleneck_dir, image_dir, jpeg_data_tensor,
                                  bottleneck_tensor):
    class_count = len(image_lists.keys())
    bottlenecks = []
    ground_truths = []
    filenames = []
    if how_many >= 0:
        # Retrieve a random sample of bottlenecks.
        for unused_i in range(how_many):
            label_index = random.randrange(class_count)
            label_name = list(image_lists.keys())[label_index]
            image_index = random.randrange(MAX_NUM_IMAGES_PER_CLASS + 1)
            image_name = directory_for_images(image_lists, label_name, image_index,
                                  image_dir, category)
            bottleneck = verify_bootleneck_existence(sess, image_lists, label_name,
                                            image_index, image_dir, category,
                                            bottleneck_dir, jpeg_data_tensor,
                                            bottleneck_tensor)
            ground_truth = np.zeros(class_count, dtype=np.float32)
            ground_truth[label_index] = 1.0
            bottlenecks.append(bottleneck)
            ground_truths.append(ground_truth)
            filenames.append(image_name)
    else:
        # Retrieve all bottlenecks.
        for label_index, label_name in enumerate(image_lists.keys()):
            for image_index, image_name in enumerate(
                image_lists[label_name][category]):
                image_name = directory_for_images(image_lists, label_name, image_index,
                                    image_dir, category)
                bottleneck = verify_bootleneck_existence(sess, image_lists, label_name,
                                              image_index, image_dir, category,
                                              bottleneck_dir, jpeg_data_tensor,
                                              bottleneck_tensor)
                ground_truth = np.zeros(class_count, dtype=np.float32)
                ground_truth[label_index] = 1.0
                bottlenecks.append(bottleneck)
                ground_truths.append(ground_truth)
                filenames.append(image_name)
    return bottlenecks, ground_truths, filenames

#we save all the bottleneck layer values we have calculated because once the values are calculated
    #we can just read those values again during training without re-calculating, this speeds up the process
def bottleneck_Caching(sess, image_lists, image_dir, bottleneck_dir,
                      jpeg_data_tensor, bottleneck_tensor):
    how_many_bottlenecks = 0
    verify_directory(bottleneck_dir)
    for label_name, label_lists in image_lists.items():
        for category in ['training', 'testing', 'validation']:
            category_list = label_lists[category]
            for index, unused_base_name in enumerate(category_list):
                verify_bootleneck_existence(sess, image_lists, label_name, index,
                                 image_dir, category, bottleneck_dir,
                                 jpeg_data_tensor, bottleneck_tensor)

                how_many_bottlenecks += 1

#TensorBoard Visualization
def summarizing_variables(var):
    with tf.name_scope('summaries'):
        mean = tf.reduce_mean(var)
        tf.summary.scalar('mean', mean)
        with tf.name_scope('stddev'):
            stddev = tf.sqrt(tf.reduce_mean(tf.square(var - mean)))
        tf.summary.scalar('stddev', stddev)
        tf.summary.scalar('max', tf.reduce_max(var))
        tf.summary.scalar('min', tf.reduce_min(var))
        tf.summary.histogram('histogram', var)

#Adding a softmax layer to the model
def introduce_last_training_layer(class_count, final_tensor_name, bottleneck_tensor):
    with tf.name_scope('input'):
        bottleneck_input = tf.placeholder_with_default(
                bottleneck_tensor, shape=[None, BN_TENSOR_SIZE],
                name='BottleneckInputPlaceholder')

        ground_truth_input = tf.placeholder(tf.float32,
                                        [None, class_count],
                                        name='GroundTruthInput')
    layer_name = 'final_training_ops'
    with tf.name_scope(layer_name):
        with tf.name_scope('weights'):
            initial_value = tf.truncated_normal([BN_TENSOR_SIZE, class_count],
                                          stddev=0.001)

            layer_weights = tf.Variable(initial_value, name='final_weights')

            summarizing_variables(layer_weights)
        with tf.name_scope('biases'):
            layer_biases = tf.Variable(tf.zeros([class_count]), name='final_biases')
            summarizing_variables(layer_biases)
        with tf.name_scope('Wx_plus_b'):
            logits = tf.matmul(bottleneck_input, layer_weights) + layer_biases
            tf.summary.histogram('pre_activations', logits)

    final_tensor = tf.nn.softmax(logits, name=final_tensor_name)
    tf.summary.histogram('activations', final_tensor)

    with tf.name_scope('cross_entropy'):
        cross_entropy = tf.nn.softmax_cross_entropy_with_logits(
                labels=ground_truth_input, logits=logits)
        with tf.name_scope('total'):
            cross_entropy_mean = tf.reduce_mean(cross_entropy)
    tf.summary.scalar('cross_entropy', cross_entropy_mean)

    with tf.name_scope('train'):
        optimizer = tf.train.GradientDescentOptimizer(FLAGS.learning_rate)
        train_step = optimizer.minimize(cross_entropy_mean)

    return (train_step, cross_entropy_mean, bottleneck_input, ground_truth_input,
              final_tensor)

#To assess the accuracy of our findings
def add_evaluation_step(result_tensor, ground_truth_tensor):
    with tf.name_scope('accuracy'):
        with tf.name_scope('correct_prediction'):
            prediction = tf.argmax(result_tensor, 1)
            correct_prediction = tf.equal(
                    prediction, tf.argmax(ground_truth_tensor, 1))
        with tf.name_scope('accuracy'):
            evaluation_step = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
    tf.summary.scalar('accuracy', evaluation_step)
    return evaluation_step, prediction

#Main function for running inception model, bottleneck and softmax
def main(_):
    if tf.gfile.Exists(FLAGS.summaries_dir):
        tf.gfile.DeleteRecursively(FLAGS.summaries_dir)
    tf.gfile.MakeDirs(FLAGS.summaries_dir)

    inception_model_files_check()
    graph, bottleneck_tensor, jpeg_data_tensor, resized_image_tensor = (
            graph_creation())
    # create list of all images
    image_lists = listing_images(FLAGS.image_dir, FLAGS.percentage_for_testing,
                                   FLAGS.percentage_for_validation)
    class_count = len(image_lists.keys())
    if class_count == 0:
        print('No valid folders of images found at ' + FLAGS.image_dir)
        return -1
    if class_count == 1:
        print('Only one valid folder of images found at ' + FLAGS.image_dir +
              ' - multiple classes are needed for classification.')
        return -1

    # calculate bottleneck values and cache them
    with tf.Session(graph=graph) as sess:
        bottleneck_Caching(sess, image_lists, FLAGS.image_dir,
                    FLAGS.bottleneck_dir, jpeg_data_tensor,
                    bottleneck_tensor)
        # adding the new softmax layer
        (train_step, cross_entropy, bottleneck_input, ground_truth_input,
         final_tensor) = introduce_last_training_layer(len(image_lists.keys()),
                                            FLAGS.final_tensor_name,
                                            bottleneck_tensor)
        # evaluate the results
        evaluation_step, prediction = add_evaluation_step(
                final_tensor, ground_truth_input)
        # merge all the summaries
        merged = tf.summary.merge_all()
        train_writer = tf.summary.FileWriter(FLAGS.summaries_dir + '/train',
                                         sess.graph)

        validation_writer = tf.summary.FileWriter(
                FLAGS.summaries_dir + '/validation')

        init = tf.global_variables_initializer()
        sess.run(init)
        # Run the training for the given number of steps
        for i in range(FLAGS.steps_for_training):
            (train_bottlenecks,
            train_ground_truth, _) = random_retrieving_bottleneck_cache(
                    sess, image_lists, FLAGS.batch_size_for_training, 'training',
                    FLAGS.bottleneck_dir, FLAGS.image_dir, jpeg_data_tensor,
                    bottleneck_tensor)
            #Add bottleneck and grounth truth values into the graph
            train_summary, _ = sess.run(
                    [merged, train_step],
                    feed_dict={bottleneck_input: train_bottlenecks,
                               ground_truth_input: train_ground_truth})
            train_writer.add_summary(train_summary, i)
            #Print accuracy and entropy values after regular intervals
            is_last_step = (i + 1 == FLAGS.steps_for_training)
            if (i % FLAGS.interval_for_step_evaluation) == 0 or is_last_step:
                train_accuracy, cross_entropy_value = sess.run(
                        [evaluation_step, cross_entropy],
                        feed_dict={bottleneck_input: train_bottlenecks,
                                   ground_truth_input: train_ground_truth})
                validation_bottlenecks, validation_ground_truth, _ = (
                        random_retrieving_bottleneck_cache(
                                sess, image_lists, FLAGS.batch_size_for_validation, 'validation',
                                FLAGS.bottleneck_dir, FLAGS.image_dir, jpeg_data_tensor,
                                bottleneck_tensor))

                validation_summary, validation_accuracy = sess.run(
                        [merged, evaluation_step],
                        feed_dict={bottleneck_input: validation_bottlenecks,
                                   ground_truth_input: validation_ground_truth})
                validation_writer.add_summary(validation_summary, i)
                print('Step: %d, Train accuracy: %.4f%%, Cross entropy: %f, Validation accuracy: %.1f%% (N=%d)' % (i,
                        train_accuracy * 100, cross_entropy_value, validation_accuracy * 100, len(validation_bottlenecks)))
        #We test if our model works correctly for a new set of images that were not used in the training process
        test_bottlenecks, test_ground_truth, test_filenames = (
                random_retrieving_bottleneck_cache(sess, image_lists, FLAGS.batch_size_for_testing,
                                      'testing', FLAGS.bottleneck_dir,
                                      FLAGS.image_dir, jpeg_data_tensor,
                                      bottleneck_tensor))
        test_accuracy, predictions = sess.run(
                [evaluation_step, prediction],
                feed_dict={bottleneck_input: test_bottlenecks,
                           ground_truth_input: test_ground_truth})
        print('Final test accuracy = %.1f%% (N=%d)' % (
                test_accuracy * 100, len(test_bottlenecks)))

        if FLAGS.print_misclassified_test_images:
            print('=== MISCLASSIFIED TEST IMAGES ===')
            for i, test_filename in enumerate(test_filenames):
                if predictions[i] != test_ground_truth[i].argmax():
                    print('%70s  %s' % (test_filename,
                              list(image_lists.keys())[predictions[i]]))
        #compose the trained graph
        graph_output_def = graph_util.convert_variables_to_constants(
                sess, graph.as_graph_def(), [FLAGS.final_tensor_name])
        with gfile.FastGFile(FLAGS.graph_output, 'wb') as f:
            f.write(graph_output_def.SerializeToString())
        with gfile.FastGFile(FLAGS.output_labels, 'w') as f:
            f.write('\n'.join(image_lists.keys()) + '\n')


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument(
        '--image_dir',
        type=str,
        default='',
        help='Directorying containing images to be classified.'
        )
    parser.add_argument(
        '--graph_output',
        type=str,
        default='/tmp/graph_output.pb',
        help='Location to store trained graph.'
        )
    parser.add_argument(
        '--output_labels',
        type=str,
        default='/tmp/output_labels.txt',
        help='Location to store trained graph labels.'
        )
    parser.add_argument(
        '--summaries_dir',
        type=str,
        default='/tmp/retrain_logs',
        help='Tensorboard summary logs directory.'
        )
    parser.add_argument(
        '--steps_for_training',
        type=int,
        default=5000,
        help='Number of training steps.'
        )
    parser.add_argument(
        '--learning_rate',
        type=float,
        default=0.5,
        help='Learning rate for model training.'
        )
    parser.add_argument(
        '--percentage_for_testing',
        type=int,
        default=20,
        help='The amount of percentage of images required for testing.'
        )
    parser.add_argument(
        '--percentage_for_validation',
        type=int,
        default=10,
        help='The amount of percentage of images required for validation.'
        )
    parser.add_argument(
        '--interval_for_step_evaluation',
        type=int,
        default=100,
        help='Steps required to evaluate the training results.'
        )
    parser.add_argument(
        '--batch_size_for_training',
        type=int,
        default=100,
        help='Number of images required to train on at a time.'
        )
    parser.add_argument(
        '--batch_size_for_testing',
        type=int,
        default=-1,
        help="""\
        Number of images to test on. This set is only used once, to determine
        the final accuracy resulted by training the model.
        In order to use entire test set, we can assign a value of -1.
        """
        )
    parser.add_argument(
        '--batch_size_for_validation',
        type=int,
        default=100,
        help="""\
        Number of images required to use in an evaluation batch. 
        In order to use entire validation set, we can assign a value of -1.
        """
        )
    parser.add_argument(
        '--print_misclassified_test_images',
        default=False,
        help="""\
        If we want to print a list of all misclassified test images.\
        """,
        action='store_true'
        )
    parser.add_argument(
        '--model_dir',
        type=str,
        default='/tmp/imagenet',
        help="""\
        Directory for classify_image_graph_def.pb,
        imagenet_synset_to_human_label_map.txt, and
        imagenet_2012_challenge_label_map_proto.pbtxt.\
        """
        )
    parser.add_argument(
        '--bottleneck_dir',
        type=str,
        default='/tmp/bottleneck',
        help='Directory for cache bottleneck layer values as files.'
        )
    parser.add_argument(
        '--final_tensor_name',
        type=str,
        default='final_result',
        help="""\
        The name of the output classification layer in the retrained graph.\
        """
        )

    FLAGS, unparsed = parser.parse_known_args()
    tf.app.run(main=main, argv=[sys.argv[0]] + unparsed)
