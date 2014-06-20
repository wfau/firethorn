import os
import glob
 
tests = glob.glob('test*.py')
for test in tests:
    os.system('python %s' % test)
