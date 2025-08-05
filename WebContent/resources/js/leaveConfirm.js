window.onbeforeunload = function() {
  return "There might be transactions ongoing. Are you sure you want to leave this page?";
}