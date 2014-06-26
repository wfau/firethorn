import os
import glob
import sys


tests = glob.glob('testing/test*.py')
for test in tests:
    os.system('python %s' % test)
