
# coding: utf-8

# In[1]:

import pandas as pd
pd.options.mode.chained_assignment = None
import numpy as np
from copy import deepcopy
from string import punctuation
from random import shuffle

# import gensim
# from gensim.models.word2vec import Word2Vec
# LabeledSentence = gensim.models.doc2vec.LabeledSentence

from tqdm import tqdm
tqdm.pandas(desc="progress-bar")

from nltk.tokenize import TweetTokenizer
tokenizer = TweetTokenizer()

# from sklearn.model_selection import train_test_split
# from sklearn.feature_extraction.text import TfidfVectorizer
# import seaborn as sb
from sklearn.preprocessing import scale
# import matplotlib.pyplot as plt
# get_ipython().magic('matplotlib inline')


# In[20]:

#Make a better classifier
def tokenize(tweet):
    try:
        tweet = tweet.lower()
        tokens = tokenizer.tokenize(tweet)
        tokens = list(filter(lambda t: t.isalpha(), tokens))
        tokens = list(filter(lambda t: not len(t)<=2, tokens))
        tokens = list(filter(lambda t: not t.startswith('@'), tokens))
        tokens = list(filter(lambda t: not t.startswith('#'), tokens))
        tokens = list(filter(lambda t: not t.startswith('http'), tokens))
        return tokens
    except:
        return 'NC'


# In[13]:

def postprocess(data, n=1000000):
    data = data.head(n)
    data['tokens'] = data['SentimentText'].progress_map(tokenize)
    data = data[data.tokens != 'NC']
    data.reset_index(inplace=True)
    data.drop('index', inplace=True, axis=1)
    return data


# In[22]:

tweet_w2v = Word2Vec.load('tweet_w2v')


# In[31]:

import pickle
with open('tfidf.pickle', 'rb') as handle:
    tfidf = pickle.load(handle)


# In[32]:

#Now let's define a function that, given a list of tweet tokens, creates an averaged tweet vector.
def buildWordVector(tokens, size):
    vec = np.zeros(size).reshape((1, size))
    count = 0.
    for word in tokens:
        try:
            vec += tweet_w2v[word].reshape((1, size)) * tfidf[word]
            count += 1.
        except KeyError: # handling the case where the token is not
                         # in the corpus. useful for testing.
            continue
    if count != 0:
        vec /= count
    return vec


# In[33]:

def labelPredictUser(testVec):
    testData = postprocess(testVec)
    n=testData.shape[0]
    testArray = np.array(testData)

    n_dim = 200
    testArray = np.array(testData.head(n).tokens)
    test_vecs_w2v = np.concatenate([buildWordVector(z, n_dim) for z in testArray])
    
    test_vecs_w2v = scale(test_vecs_w2v)
    from keras.models import load_model
    mymodel = load_model('mymodel.h5')
    return mymodel.predict(test_vecs_w2v), mymodel.predict_classes(test_vecs_w2v)


# In[34]:

def makeUserDF(fileName):
    user = pd.read_csv(fileName,error_bad_lines=False,sep=';')
    user = user.filter(['text'])
    user.columns = ["SentimentText"]
    user = user[user['SentimentText'].isnull() == False]
    user.reset_index(inplace=True)
    user.drop('index', axis=1, inplace=True)
    print ('dataset loaded with shape', user.shape)   
    return user


# In[35]:

def stats(name,fileName):
    user = makeUserDF(fileName)
    predProb, predLabel = labelPredictUser(user)
    val = np.sum(predProb)/float(len(predProb))
    st = ""
    posCount = 0
    negCount = 0
    for i in range(len(predProb)):
        if predProb[i] >= 0.9:
            negCount += 1
        else:
            posCount += 1
    print(posCount,negCount,name + " intensity score : " + str(val) + st)
    return [name,posCount, negCount, str(val)]


# In[36]:

import sys
sys.path.insert(0,'/GetOldTweets-python-master')
import Exporter


# In[37]:

def getUserTweets(username):
    import os
    import sys
    sys.path.insert(0,'/GetOldTweets-python-master')
    import Exporter
    os.system('python3 /GetOldTweets-python-master/Exporter.py --username ' + username + ' --maxtweets 5000')
    return stats(username,'usertweets.csv')
getUserTweets("ask_adarsh")


# In[ ]:




# In[ ]:




# In[ ]:




# In[ ]:




# In[ ]:



