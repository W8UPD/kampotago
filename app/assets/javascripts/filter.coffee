$ ->
  $('input[name=callsign]').focus()
  $('input[name=callsign]').keyup ->
    $("#contacts td.callsign:Contains('" + $(this).val() + "')").parent().show()
    $("#contacts td.callsign:not(:Contains('" + $(this).val() + "'))").parent().hide()

@ajaxDeleteID = (id) ->
  $.ajax "/delete/#{id}",
    "type": "DELETE",
    "success": (xhr, status) ->
      alert("HTTP DELETE'd ID# #{id}")
    "error": (xhr, status, error) ->
      alert("ERROR: #{error}")
