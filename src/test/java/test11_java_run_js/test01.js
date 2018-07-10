
// init
var flag = false;


//1
function add(x, y) {
	//print(flag);
	if (flag) {
		return x + y;
	} else {
		return 0;
	}
}
add(1, 2);


//2
function reverse(name) {
	var output = "";
	for (i = 0; i <= name.length; i++) {
		output = name.charAt(i) + output
	}
	return output;
}


