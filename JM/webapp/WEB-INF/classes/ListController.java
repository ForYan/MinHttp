import com.For.JM.http.Controller;
import com.For.JM.http.Request;
import com.For.JM.http.Response;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException; 

//List只需要只支持get方法，因为相当是查询
public class ListController extends Controller{
	List<Article> list=new Database().getList();
	@Override
	public void doGet(Request request,Response response)throws IOException{
		
		response.println("<html>");
		response.println("<body>");
		response.println("<ol>");
		
		for(Article article:list){//list 里面存的是每一篇文章的id，title和content
			response.println("<li><a href='/article?id=" + article.id + "'>"+article.title+"</a></li>");
		}
		
		response.println("</ol>");
		response.println("</body>");
		response.println("</html>");
	}
}