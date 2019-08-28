import com.For.JM.http.Controller;
import com.For.JM.http.Request;
import com.For.JM.http.Response;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException; 
import com.For.JM.http.Status;
public class ArticleController extends Controller{
	@Override
	public void doGet(Request request,Response response)throws IOException{
		//文章的url：/id=？
		String id=request.requestParams.get("id");//?改过
		if(id==null){
			response.status=Status.Not_Found;
			response.println("没有该文章");
			return;
		}
	
		Article article=new Database().getArticle(id);
		
		response.println("<html>");
		response.println("<body>");
		response.println("<h1>"+article.title+"</h1>");
	//	response.println("<p>"+article.author+"</p>");
//		response.println("<p>"+article.date+"</p>");
		response.println("<p>"+article.content+"</p>");
		response.println("</body>");
		response.println("</html>");
	}
}