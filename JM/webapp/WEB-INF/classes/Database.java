import redis.clients.jedis.Jedis;
import java.util.ArrayList;
import java.util.List;

public class Database{

private String getArticleKey(String id){
	return String.format("article_%s",id);
}
	
	public List<Article> getList(){
		Jedis jedis=new Jedis("127.0.0.1");
		List<String> ids=jedis.lrange("article_id_list",0,-1);
		List<Article> list=new ArrayList<>(ids.size());
		for(String id: ids){//ids里面存的都是id
			Article article=new Article();
			article.id=id;//取出它的id
			article.title=jedis.hget(getArticleKey(id),"title");
		//	article.author=jedis.hget(getArticleKey(id),"author");
			article.content=jedis.hget(getArticleKey(id),"content");
		//	article.date=jedis.hget(getArticleKey(id),"date");
			
			list.add(article);
		}
		return list;//返回的是每一篇文章 
	}
	public Article getArticle(String id){
		Jedis jedis=new Jedis("127.0.0.1");
		Article article=new Article();
		article.id=id;
		article.title=jedis.hget(getArticleKey(id),"title");
		//article.author=jedis.hget(getArticleKey(id),"author");
		article.content=jedis.hget(getArticleKey(id),"content");
		//article.date=jedis.hget(getArticleKey(id),"date");
		
		return article;
	}
}