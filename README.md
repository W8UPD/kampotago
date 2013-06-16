# Kampo Tago

(Esperanto for 'fieldday')

Kampo Tago is a Play 2.1 application written in Scala for the purposes of
handling ARRL Field Day logging, and only ARRL Field Day logging.

It uses websockets so that multiple participants can always see the latest
log entries, automatically.

Kampo Tago has an extreme focus on simplicity. It does some **small things**
and (hopefully) does them well:

- **Dupe checking**
  - As you type, the list of contacts will filter down as what you type matches
    already logged callsigns.
- **Multiuser access**
  - As field day typically has several stations set up, this was a priority
    goal.
    As you log, your log entries are shown to everyone who has the logbook
    open.
    If you delete an entry, it deletes on everyone's screen as well.
  - **TODO:** We could add inplace editing fairly trivially. Parse out the
    contact ID from the `<tr>`'s id, note the new value and field name, shove
    the field name and value in a `ContactEdit` or similar case class, then
    send that down the actor path and write a few lines of JS to handle it
    from the websocket. Trivial, and maybe useful for next year.
- **Stability**
  - The app is meant to be pretty solid. Even if something dies, the app should
    recover in a clean way (and in most cases, alert the user who caused an
    error condition).

It is (c) 2013 Ricky Elrod, and released under the MIT license.
