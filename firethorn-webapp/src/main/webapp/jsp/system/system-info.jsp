<%@ page
    import="org.joda.time.DateTime"
    import="org.joda.time.format.DateTimeFormatter"
    import="org.joda.time.format.ISODateTimeFormat"
    import="java.util.Properties"
    import="org.jsoftbiz.utils.OS"
    contentType="application/json" 
    session="false"
%><%!
private static final DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
private static final OS os = OS.getOs();
%><%
final Properties props = new Properties();
try {
    props.load(
        this.getClass().getResourceAsStream(
            "/firethorn-build.properties"
            )
        );
    }
catch (Exception ouch)
    {
    }
%>{
"java": {
    "name" : "<%= System.getProperty("java.vm.name")    %>",
    "build" : "<%= System.getProperty("java.vm.version") %>",
    "version" : "<%= System.getProperty("java.version")    %>"
    },
"build": {
    "name" : "<%= props.get("firethorn.build.name") %>",
    "version" : "<%= props.get("firethorn.build.version") %>",
    "timestamp" : "<%= props.get("firethorn.build.timestamp") %>",
    "changeset" : "<%= props.get("firethorn.build.changeset").toString().replace("+", "") %>"
    },
"system": {
    "time" : "<%= formatter.print(new DateTime()) %>",
    "name" : "<%= os.getName() %>",
    "arch" : "<%= os.getArch() %>",
    "version"  : "<%= os.getVersion() %>",
    "platform" : "<%= os.getPlatformName() %>"
    },
"servlet": {
    "server" : "<%= application.getServerInfo() %>",
    "context" : "<%= application.getContextPath() %>"
    }
}

