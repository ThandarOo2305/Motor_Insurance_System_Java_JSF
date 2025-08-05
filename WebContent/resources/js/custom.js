/**
 * 
 */
function isNumericKey(evt) {
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
		return false;

	return true;
}



function isFormulaKey(evt){
	var ValidPattern = /^[\d\{\}\+\-\*\/\.\,]{1}$/;
	var str = String.fromCharCode(evt.keyCode);
	if(ValidPattern.test(str)) return true;
	
	return false;
}

function isDecimalKey(evt,txtValue)
{
    var charCode = (evt.which) ? evt.which : event.keyCode
	    if (charCode == 46)
	    {
	        var inputValue = txtValue.value;
	        if (inputValue.indexOf('.') > 1)
	        {
	        	evt.preventDefault();
	        }
	    }
    	// 45 is - (minus)
	    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) && (charCode!=109 || charCode!=45))
	    {
	    	evt.preventDefault();
	    }
};


