import com.alibaba.fastjson.JSONArray;
import com.wust.entity.Bo.CommentBo;
import com.wust.utils.JsonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JsonTest {

	@Test
	public void test(){
		String jsonData = "";

		List<CommentBo> currLists = new ArrayList<>();
		JSONArray jArray= JSONArray.parseArray(jsonData);
		Collection collection = JSONArray.parseArray(jArray.toJSONString(), CommentBo.class);
		Iterator it = collection.iterator();
		while (it.hasNext()) {

			System.out.println("================"+it.next());

			CommentBo currContent = (CommentBo) it.next();

			System.out.println("================"+currContent);
			currLists.add(currContent);

		}
	}
}
