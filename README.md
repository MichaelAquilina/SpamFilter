Spam Filter
===========

Project for "Introduction to Machine Learning" course.

Results
-------

Current terms selected from feature selection: https://gist.github.com/KillaW0lf04/d430834e07b4e7aa3901

Design
------

A [design wiki](https://github.com/KillaW0lf04/SpamFilter/wiki/Design) has been set up to establish a common understanding of components and milestones needed to complete the project. This should also help us when it comes to writing the report for hand in.

Document Classfication
----------------------

C# Project for document classification https://github.com/KillaW0lf04/Document-Classification
Should be able to provide us with some good ideas about how to improve classification for the second stage.

Requirements
------------

Requirements can be found in the following pages:
* [Spam Filter Implementation](https://www.cs.bris.ac.uk/Teaching/Resources/COMS30301/projects/spam/1/index.html)
* [Training and Classification](https://www.cs.bris.ac.uk/Teaching/Resources/COMS30301/projects/spam/2/index.html)

Summary
-------
* The project should be implemented in java
* main file should be called `filter.java` and take only one argument

* The program will be tested using an automatic marking script on a test dataset. A sample of the automarking script can be found 
  [here](https://www.cs.bris.ac.uk/Teaching/Resources/COMS30301/projects/spam/2/sampletest.tar.gz).

Notes from 28/11/13
-------------------

Implement Naive Bayes classifier seperately. Upload both implementations for comparasin.

We shall be using a Bag of Words approach for features in the emails (With the possibility of adding extra meta-data). 

Other possible features:
* Numbers 
* Currency (Identifies the "Buy now for only 5$!" type emails)
* Email meta-data such as PGP
* Number of links
* Images

We can also use the [Term Frequency Inverse Document Frequency (TFIDF)](https://gist.github.com/KillaW0lf04/7720122) equation which has proved useful before


Main Programming Components:
* Suffix Tree (Inverted Index)
* Email Parser
  * Should return an Email/Document class with an ArrayList<string>
  * Output should be either UTF-8 or ASCII (Need to test this, maybe provide a flag to disable or enable)?
* Classify Interface
  * Should contain methods for "Classify(Email)" and "Train(LabeledEmails)"
* Classifier (To be decided)

Pre-processing techniques:
* Spell correction (use a library)
* Stemming / Lemmatisation ([Porter Stemmer Algorithm](http://snowball.tartarus.org/algorithms/english/stemmer.html))
* Stop word removal
* Use a Zipfian Distribution to reduce dimensions
* Remove words which are equally likely in ham and spam

Papers:
* http://www.icmc.usp.br/pessoas/mcmonard/public/iastedCE2003.pdf

Third Party Code
* [Porter Stemmer Algorithm](http://tartarus.org/martin/PorterStemmer/) in [java](http://tartarus.org/martin/PorterStemmer/java.txt)
