package modules.forg.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;
/**
 * 
 * @author auto
 * @createDate 2018-02-09 16:45:49
 **/
@Table(name="forg_bainian_replay")
public class ForgBainianReplayDDL{
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="id", type=DbType.Int)
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id){
		this.id=id;
	}

	@Column(name="replay_id", type=DbType.Int)
	private Integer replayId;
	public Integer getReplayId() {
		return replayId;
	}
	public void setReplayId(Integer replayId){
		this.replayId=replayId;
	}

	@Column(name="replay_voice_osskey", type=DbType.Varchar)
	private String replayVoiceOsskey;
	public String getReplayVoiceOsskey() {
		return replayVoiceOsskey;
	}
	public void setReplayVoiceOsskey(String replayVoiceOsskey){
		this.replayVoiceOsskey=replayVoiceOsskey;
	}

	@Column(name="replayer_nick_name", type=DbType.Varchar)
	private String replayerNickName;
	public String getReplayerNickName() {
		return replayerNickName;
	}
	public void setReplayerNickName(String replayerNickName){
		this.replayerNickName=replayerNickName;
	}

	@Column(name="replayer_avater", type=DbType.Varchar)
	private String replayerAvater;
	public String getReplayerAvater() {
		return replayerAvater;
	}
	public void setReplayerAvater(String replayerAvater){
		this.replayerAvater=replayerAvater;
	}

	@Column(name="replay_time", type=DbType.DateTime)
	private Long replayTime;
	public Long getReplayTime() {
		return replayTime;
	}
	public void setReplayTime(Long replayTime){
		this.replayTime=replayTime;
	}

	public static ForgBainianReplayDDL newExample(){
		ForgBainianReplayDDL object=new ForgBainianReplayDDL();
		object.setId(null);
		object.setReplayId(null);
		object.setReplayVoiceOsskey(null);
		object.setReplayerNickName(null);
		object.setReplayerAvater(null);
		object.setReplayTime(null);
		return object;
	}
}
