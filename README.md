# Kampo Tago

(Esperanto for 'fieldday')

Kampo Tago is a Play 2.1 application written in Scala for the purposes of
handling ARRL Field Day logging, and only ARRL Field Day logging.

It uses web sockets so that multiple participants can always see the latest
log entries, automatically.

It is (c) 2013 Ricky Elrod, and released under the MIT license.

# Ideas

Start typing the callsign. Once you pause for 500ms, search the db for
contacts made to that call, and display **all** of them. This removes
the requirement that the user set the band and mode beforehand.

