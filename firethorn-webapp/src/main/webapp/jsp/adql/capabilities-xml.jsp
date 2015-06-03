
<?xml version="1.0" encoding="UTF-8"?>

<vosi:capabilities xmlns=""

   xmlns:vosi="http://www.ivoa.net/xml/VOSI/v1.0"

   xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0"

   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"

   xsi:schemaLocation="http://www.ivoa.net/xml/VOSI/v1.0

                       http://www.ivoa.net/xml/VOSI/v1.0

             http://www.ivoa.net/xml/VODataService/v1.0

             http://www.ivoa.net/xml/VODataService/v1.0">

  <vosi:capability standardID="ivo://ivoa.net/std/TAP">

    <interface xsi:type="vs:ParamHTTP" role="std">

      <accessURL use="base"> http://myarchive.net/myTAP </accessURL>

    </interface>

  </vosi:capability>

  <vosi:capability standardID="ivo://ivoa.net/std/VOSI#capabilities">

    <interface xsi:type="vs:ParamHTTP">

      <accessURL use="full">

        http://myarchive.net/myTAP/capabilities </accessURL>

    </interface>

  </vosi:capability>

  <vosi:capability standardID="ivo://ivoa.net/std/VOSI#availability">

    <interface xsi:type="vs:ParamHTTP">

      <accessURL use="full">

        http://myarchive.net/myTAP/availability

      </accessURL>

    </interface>

  </vosi:capability>

  <vosi:capability standardID="ivo://ivoa.net/std/VOSI#tables">

    <interface xsi:type="vs:ParamHTTP">

      <accessURL use="full">

        http://myarchive.net/myTAP/tables </accessURL>

    </interface>

  </vosi:capability>

</vosi:capabilities>