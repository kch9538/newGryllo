async function chatSave(data, selectedUserId) {
	let response = await fetch("/chatSave/" + selectedUserId, {
		method: "post",
		body: data,
		headers: {
			"Content-Type": "text/html"
		}
	}).then(function(res) {
		return res.text();
	}).then(function(res) {
	});

}

