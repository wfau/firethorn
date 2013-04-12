from paste.fixture import TestApp
from nose.tools import *
from app import app
from datetime import datetime
import re

sample_workspace_url = "http%3A%2F%2Flocalhost%3A8080%2Ffirethorn%2Fadql%2Fresource%2F4"

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
        
        
def test_workspace():
  
    # check that we get a 404 on the / URL
    resp = app.request("/workspace")
    assert_response(resp, status="200",contains="Welcome to your workspaces")
 
    resp = app.request("/vospace?path=" + sample_workspace_url + "&mode=getfolder&showThumbs=true&time=777&type=http://data.metagrid.co.uk/wfau/firethorn/types/jdbc-resource-1.0.json&parent_folder=&workspace=" + sample_workspace_url, method="GET")
    assert_response(resp, contains="result")
    assert_response(resp, contains="name")
    assert_response(resp, contains="root_type")
    

def test_run_query():
    resp = app.request("/vospace?mode=run_query&query=select%201&query_name=1&workspace=" + sample_workspace_url, method="GET")
    assert_response(resp, contains="PARSE_ERROR")
  
    
    
def test_get_metadata():
    resp = app.request("/vospace?mode=get_metadata&ident="  + sample_workspace_url + "&workspace=&_type=http%3A%2F%2Fdata.metagrid.co.uk%2Fwfau%2Ffirethorn%2Ftypes%2Fadql-resource-1.0.json", method="GET")
    assert_response(resp, contains="Created on")
   