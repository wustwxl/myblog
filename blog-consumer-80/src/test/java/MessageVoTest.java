import com.wust.vo.MessageVo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageVoTest {

	@Test
	public void test(){
		MessageVo curr = new MessageVo();
		System.out.println(curr);
	}

	@Test
	public void testLinkedHashMap(){
		List<MessageVo> curr = new ArrayList<>();
		curr.add(new MessageVo());
		curr.add(new MessageVo());
		curr.add(new MessageVo());

		System.out.println(curr);
	}

}
