import sys
import os
import json
import numpy
import math


def sigmoid(z):
    print(1.0 / (1 + math.exp(-z)))

if __name__ == '__main__':
    sigmoid(2)