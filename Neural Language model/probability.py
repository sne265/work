
# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

import data
import argparse
import torch
import os

parser = argparse.ArgumentParser(description='PyTorch Wikitext-2 RNN/LSTM Language Model')
parser.add_argument('--input',type=str, default='./data',
                    help='path leading to the directory that contains input text files')
parser.add_argument('--data', type=str, default='./data',
                    help='location of the data corpus')
parser.add_argument('--outf', type=str, default='output.tsv',
                    help='tsv output file to store probabilities')
parser.add_argument('--checkpoint', type=str, default='./model.pt',
                    help='saved model to be loaded,checkpoint saved during training')
parser.add_argument('--cuda', action='store_true',
                    help='use CUDA')
#parser.add_argument('--bptt', type=int, default=1,
#                    help='sequence length')
args = parser.parse_args()
if torch.cuda.is_available():
    if not args.cuda:
        print("WARNING: You have a CUDA device, so you should probably run with --cuda")

device = torch.device("cuda" if args.cuda else "cpu")

# create vocab
corpus = data.Corpus(args.data)
ntokens = len(corpus.dictionary)

# initialize required parameters here
eval_batch_size = 1
softmax = torch.nn.Softmax(dim = -1)

with open(args.checkpoint, 'rb') as f:
    model = torch.load(f,map_location='cpu').to(device)

is_transformer_model = hasattr(model, 'model_type') and model.model_type == 'Transformer'

def get_index(word):
    if word in corpus.dictionary.word2idx:
         #print(corpus.dictionary.word2idx[word])
         return corpus.dictionary.word2idx[word]
    else:
         return corpus.dictionary.word2idx["<unk>"]

def batchify(data, bsz):
    # Work out how cleanly we can divide the dataset into bsz parts.
    nbatch = data.size(0) // bsz
    # Trim off any extra elements that wouldn't cleanly fit (remainders).
    data = data.narrow(0, 0, nbatch * bsz)
    # Evenly divide the data across the bsz batches.
    data = data.view(bsz, -1).t().contiguous()
    #print(data.shape)
    return data.to(device)

'''def get_batch(source, i):
    seq_len = min(args.bptt, len(source) - 1 - i)
    data = source[i:i+seq_len]
    target = source[i+1:i+1+seq_len].view(-1)
    return data, target
'''
def repackage_hidden(h):
    """Wraps hidden states in new Tensors, to detach them from their history."""

    if isinstance(h, torch.Tensor):
        return h.detach()
    else:
        return tuple(repackage_hidden(v) for v in h)

def text_to_word_ids(path):
	with open(path, 'r', encoding="utf8") as f:
            idss = []
            for line in f:
                words = line.split()
                ids = []
                for word in words:
                    ids.append(get_index(word))
                idss.append(torch.tensor(ids).type(torch.int64))
            ids = torch.cat(idss)  
	return ids

def prepare_data(path):
    ids_list = text_to_word_ids(path)
    test_data = batchify(ids_list, eval_batch_size)
    return test_data

def get_probability(input_filepath,output_filepath):

    input_data = prepare_data(input_filepath)

    # Turn on evaluation mode which disables dropout.
    model.eval()

    if not is_transformer_model:
        hidden = model.init_hidden(eval_batch_size)

    with torch.no_grad(),open(output_filepath, 'w') as outfile,open(input_filepath, 'r', encoding="utf8") as inputfile:

        i =0
        line_id=-1
        outfile.write("\nLINE_NUMBER\t\t\tWORD_INDEX\t\t\tPROBABILITY\n")

        for line in inputfile:

              line_id += 1
              words = line.split() #+ ['<eos>']

              seq_len =  len(words)

              if seq_len>0:
                 data = input_data[i:i+seq_len]

                 if is_transformer_model:
                    output = model(data)
                 else:                                  #output is linear o/p of shape  seq_len,batch,ntoken
                    output, hidden = model(data, hidden)  #encode o/p seq_len, batch, num_directions * hidden_size,linear o/p se
                    hidden = repackage_hidden(hidden)
                 #print(output.shape)
                 #print(output)
                 probabilities = softmax(output)

                 for j,wrd_idx in enumerate(data): #target

                     wrd_prob = probabilities[j,0,wrd_idx].item()
                     #print(wrd_prob)
                     #print("word=",wrd_idx,"output_idx:",output[j,0,wrd_idx])

                     outfile.write("\n\t") #Line {}\t\tword_pos:{}\t\tprobability:{0:.2f}".format(line_id, wrd_idx,wrd_prob))
                     outfile.write(str(line_id))
                     outfile.write("\t\t\t")
                     outfile.write(str(j))
                     outfile.write("\t\t\t\t")
                     outfile.write(str(round(wrd_prob,5)))
              i=i+seq_len


##### check input file path and create output filepath for saving probabilities ######

#### for wikitext folder containing train,valid and test data, to use only test data

if args.input == './data':

     input_path = os.path.join(args.input, 'test.txt')

     output_directory=os.path.splitext(os.path.basename(args.checkpoint))[0]+"_output"

     if not os.path.exists(output_directory):
         os.makedirs(output_directory)

     output_path = os.path.join(output_directory,os.path.splitext(os.path.basename(args.input))[0]+".tsv")
     get_probability(input_path,output_path)

else:
     ###### for other input directories with multiple text files

     # get input directory
     dir_name = args.input
     input_directory = os.fsencode(args.input)

     #  create appropriate output folder for a particular model using saved model name
     out_dir = os.path.splitext(os.path.basename(args.checkpoint))[0]+"_output"
     sub_dir = os.path.splitext(os.path.basename(args.input))[0]+"_output"

     output_directory = os.path.join(out_dir,sub_dir)

     if not os.path.exists(output_directory):
         os.makedirs(output_directory)

     #print(output_directory)
     for file in os.listdir(input_directory):
          filename = os.fsdecode(file)
          #print(os.path.join(directory_in_str, filename))
          if filename.endswith(".txt"):
              input_path = os.path.join(args.input, filename)
              output_path =os.path.join(output_directory,os.path.splitext(os.path.basename(filename))[0]+".tsv")
              get_probability(input_path,output_path)
