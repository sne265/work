####################################################
# Pre-requisities for running Image Classification #
####################################################
1. Make sure the below packages is installed in Anaconda
-argparse
-hashlib
-os.path
-os
-random
-re
-struct
-sys
-tarfile
-numpy
-six.moves
-tensorflow
-tensorflow.python.framework
-tensorflow.python.platform
-tensorflow.python.util

2. Make sure the below files are available in the directory
-classify.py
-run.sh
-train.py

3. Make sure the below folders are available in the directory where the scripts are present
-dataset
-inception
-test


####################################################
#    Steps for running the code - Initial Run      #
####################################################
1. Navigate to the directory where the above files and folders are present.
2. Make sure the mode of the files and folders are 755. If not please use the command: chmod -R 755 *
3. Run the shell script as follows(On Linux/Mac System): ./run.sh
   Run the batch script as follows(On Windows System)  : ./run.cmd
4. Once the script is executed, run the below commands to classify the test images:
python classify.py ./test/apple.jpg
python classify.py ./test/canta.jpg
python classify.py ./test/HB.jpg
python classify.py ./test/lyche.jpg


##########################################################
# Steps for running the code - With different parameters #
##########################################################
1. Update the required parameters under FLAGS in train.py
2. Rename/delete logs folder created in the initial run
3. Navigate to the directory where the above files and folders are present.
4. Make sure the mode of the files and folders are 755. If not please use the command: chmod -R 755 *
5. Run the shell script as follows(On Linux/Mac System): ./run.sh
   Run the batch script as follows(On Windows System)  : ./run.cmd
6. Once the script is executed, run the below commands to classify the test images:
python classify.py ./test/apple.jpg
python classify.py ./test/canta.jpg
python classify.py ./test/HB.jpg
python classify.py ./test/lyche.jpg