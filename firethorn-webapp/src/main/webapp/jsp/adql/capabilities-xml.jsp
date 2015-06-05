<%@ page
    import="uk.ac.roe.wfau.firethorn.webapp.tap.UWSJobFactory"
    contentType="text/xml; charset=UTF-8" 
    session="false"
%><?xml version="1.0" encoding="UTF-8"?>
<vosi:capabilities xmlns:vosi="http://www.ivoa.net/xml/VOSICapabilities/v1.0" xmlns:vod="http://www.ivoa.net/xml/VODataService/v1.1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <capability standardID="ivo://ivoa.net/std/VOSI#capabilities">
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="full">http://localhost:8080/firethorn/tap/2523137/capabilities</accessURL>
    </interface>
  </capability>
  <capability standardID="ivo://ivoa.net/std/VOSI#availability">
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="full">http://localhost:8080/firethorn/tap/2523137/availability</accessURL>
    </interface>
  </capability>
  <capability standardID="ivo://ivoa.net/std/VOSI#tables">
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="full">http://localhost:8080/firethorn/tap/2523137/tables</accessURL>
    </interface>
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="full">http://localhost:8080/firethorn/tap/2523137/auth-tables</accessURL>
      <securityMethod standardID="http://www.w3.org/Protocols/HTTP/1.0/spec.html#BasicAA"/>
    </interface>
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="full">http://localhost:8080/firethorn/tap/2523137/tables</accessURL>
      <securityMethod standardID="ivo://ivoa.net/sso#tls-with-certficate"/>
    </interface>
    <!-- RESTful VOSI-tables-1.1 would be use="base" -->
  </capability>
  <!-- TAP-1.1 sync -->
  <capability standardID="ivo://ivoa.net/std/TAP#sync-1.1">
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="base">http://localhost:8080/firethorn/tap/2523137/sync</accessURL>
    </interface>
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="base">http://localhost:8080/firethorn/tap/2523137/auth-sync</accessURL>
      <securityMethod standardID="http://www.w3.org/Protocols/HTTP/1.0/spec.html#BasicAA"/>
    </interface>
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="base">http://localhost:8080/firethorn/tap/2523137/sync</accessURL>
      <securityMethod standardID="ivo://ivoa.net/sso#tls-with-certficate"/>
    </interface>
   </capability>
  <!-- TAP-1.1 async -->
  <capability standardID="ivo://ivoa.net/std/TAP#async-1.1">
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="base">http://localhost:8080/firethorn/tap/2523137/async</accessURL>
    </interface>
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="base">http://localhost:8080/firethorn/tap/2523137/auth-async</accessURL>
      <securityMethod standardID="http://www.w3.org/Protocols/HTTP/1.0/spec.html#BasicAA"/>
    </interface>
    <interface xsi:type="vod:ParamHTTP" role="std" version="1.0">
      <accessURL use="base">http://localhost:8080/firethorn/tap/2523137/async</accessURL>
      <securityMethod standardID="ivo://ivoa.net/sso#tls-with-certficate"/>
    </interface>
    </capability>
 
</vosi:capabilities>