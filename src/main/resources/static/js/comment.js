function commentSend(imageId, imageUsername, imageUserId) {
	let content = $("#content-"+imageId).val();

	if (content == "" || content == null) {
		alert("댓글 입력이 필요합니다.");
		return;
	}

	let data = $("#frm-"+imageId).serialize();
	console.log(1, data);

	fetch("/comment", {
		method: "post",
		body: data,
		headers: {
			"Content-Type": "application/x-www-form-urlencoded; charset=utf-8"
		}
	}).then(function (res) {
		return res.text();
	}).then(function (res) {
		 send(LoginUser.username, imageUsername, "님이 회원님의 게시물에 댓글을 달았습니다", true, "comment", imageUserId, imageId);
		 location.reload();
	});
}

function commentDelete(commentId) {
	fetch("/comment/"+commentId, {
		method: "delete"
	}).then(function (res) {
		return res.text();
	}).then(function (res) {
		location.reload();
	});
}