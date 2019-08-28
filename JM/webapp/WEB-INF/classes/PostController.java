import com.For.JM.http.Controller;
import com.For.JM.http.Request;
import com.For.JM.http.Response;
import java.io.IOException; 
import redis.clients.jedis.Jedis;
import java.util.List;
import java.util.UUID;
import com.For.JM.http.Status;
public class PostController extends Controller{
	
	@Override
	public void doGet(Request request,Response response)throws IOException{}
	
	@Override
	public void doPost(Request request,Response response)throws IOException{
		String title=request.params.get("title");
    	String content=request.params.get("content");
		String id=UUID.randomUUID().toString();
	
		String key="article_"+id;
		Jedis jedis=new Jedis("127.0.0.1");
		jedis.hset(key,"title",title);
		jedis.hset(key,"content",content);
		
		jedis.lpush("article_id_list",id);
		
		response.status = Status.Redirect;
		response.setHeader("Location", "/article?id=" + id);
		
	}
	
}