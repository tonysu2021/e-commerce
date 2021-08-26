package com.commerce.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveComResponse {

	@JsonProperty("create_time")
	private String createTime;
	/** 留言編號 */
	private String id;
	/** 留下這則留言的使用者 */
	private User from;
	/** 留言內文 */
	private String message;
	/** 瀏覽者能否移除這則留言 */
	@JsonProperty("can_remove")
	private boolean canRemove;
	/** 瀏覽者能否隱藏這則留言 */
	@JsonProperty("can_hide")
	private boolean canHide;
	/** 瀏覽者能否對這則留言按讚 */
	@JsonProperty("can_like")
	private boolean canLike;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	public boolean isCanHide() {
		return canHide;
	}

	public void setCanHide(boolean canHide) {
		this.canHide = canHide;
	}

	public boolean isCanLike() {
		return canLike;
	}

	public void setCanLike(boolean canLike) {
		this.canLike = canLike;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", from=" + from + ", message=" + message + "]";
	}

}

class User {
	/** 標註的個人檔案編號 */
	private String id;
	/** 標籤中使用的文字 */
	private String name;
	/** 標註文字的第一個字元是否在 message 中 */
	private int offset;
	/** 調整之後，這個標籤包含多少個 Unicode 字碼指標 */
	private int length;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}
}
