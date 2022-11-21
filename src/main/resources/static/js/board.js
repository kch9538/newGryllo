async function boardDelete(imageId, imageUserId) {
	let response = await fetch("/image/"+imageId +"/" + imageUserId, {
		method: "delete"
  });
  let result = await response.text();
  if(result === "ok"){
    alert("게시물이 삭제 되었습니다.");
    history.back();
  }
  else{
    alert(result);
  }
}
