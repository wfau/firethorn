

cd /home/pyrothorn/

python testing/test_firethorn_logged_txt.py > logfile.txt

# Run test as background task (Closing terminal will not cancel run)
# nohup python testing/test_firethorn_logged_txt.py > logfile.txt 2>&1 </dev/null &
