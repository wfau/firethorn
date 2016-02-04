cat <<EOF >> /etc/apache2/apache2.conf
# mod_proxy setup.
ProxyRequests Off
ProxyPreserveHost On
ProxyPassMatch ^/(firethorn\/tap\/.*)$   http://${gillianip:?}:8080/$1 retry=0
ProxyPassReverse  ^/(firethorn\/tap\/.*$   http://${gillianip:?}:8080/$1

ProxyPassMatch ^/(firethorn\/adql\/table\/.*\/votable)$1   http://${gillianip:?}:8080/ retry=0
ProxyPassReverse  ^/(firethorn\/adql\/table\/.*\/votable)$1 http://${gillianip:?}:8080/

<Proxy *>
Order deny,allow
Allow from alll
</Proxy>

<Location "/firethorn">
# Configurations specific to this location. Add what you need.
# For instance, you can add mod_proxy_html directives to fix
# links in the HTML code. See link at end of this page about using
# mod_proxy_html.

# Allow access to this proxied URL location for everyone.
Order allow,deny
Allow from all
</Location>
EOF


sudo service apache2 reload
