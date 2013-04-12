from paste.fixture import TestApp
from nose.tools import *
from app import app
from datetime import datetime
import re

def assert_response(resp, contains=None, matches=None, headers=None, status="200"):

    assert status in resp.status, "Expected response %r not in %r" % (status, resp.status)

    if status == "200":
        assert resp.data, "Response data is empty."

    if contains:
        assert contains in resp.data, "Response does not contain %r" % contains

    if matches:
        reg = re.compile(matches)
        assert reg.matches(resp.data), "Response does not match %r" % matches

    if headers:
        assert_equal(resp.headers, headers)
        
        
def test_index():
    # check that we get a 404 on the / URL
    resp = app.request("/")
    assert_response(resp, status="200",contains="Home")

   
   
    data = {'db_select_with_text' :   'w', 'db_type'  :  'all'}
    resp = app.request("/index", method="POST",  data=data)
    assert_response(resp, contains="sub_item")

    data = {'db_select_with_text' :   '$$placeholder$$$', 'db_type'  :  'all'}
    resp = app.request("/index", method="POST",  data=data)
    assert_response(resp, contains="No databases found")
    
    
def test_create_new():
    
    # check that we get a 404 on the / URL
    resp = app.request("/create_new?obj_type=Workspace")
    assert_response(resp, status="200",contains="Create new Workspace")

   
    now = str(datetime.now())
    # make sure default values work for the form
    data = {'obj_name' :   'w-' + now, 'obj_type'  :  'Workspace'}
     
    resp = app.request("/create_new", method="POST",  data=data)
    resp_match = """<br /><br /><div class='notify'>Workspace <a href='http://localhost:8090/workspace?_id=w-""" + now +"""'>w-""" + now +"""</a> has been successfully created.</div><br />"""
    assert_response(resp, contains=resp_match)
    
   
