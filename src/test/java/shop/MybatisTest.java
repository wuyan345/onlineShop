package shop;

import static org.junit.Assert.*;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.shop.dao.CartMapper;
import com.shop.pojo.Cart;

/**
 * 测试失败，mybatisConfig.xml配置错误
 * @author WY
 *
 */
public class MybatisTest {

	private static SqlSessionFactory sqlSessionFactory;

	@BeforeClass
	public static void setUp() throws Exception {
		// create an SqlSessionFactory
		Reader reader = Resources.getResourceAsReader("mybatisConfig.xml");
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		reader.close();

		// populate in-memory database
//		SqlSession session = sqlSessionFactory.openSession();
//		Connection conn = session.getConnection();
//		reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/basetest/CreateDB.sql");
//		ScriptRunner runner = new ScriptRunner(conn);
//		runner.setLogWriter(null);
//		runner.runScript(reader);
//		conn.close();
//		reader.close();
//		session.close();
	}

	@Test
	public void shouldGetACart() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CartMapper cartMapper = sqlSession.getMapper(CartMapper.class);
			Cart cart = cartMapper.selectByPrimaryKey(301);
			System.out.println(cart.getPrice());
//			Assert.assertEquals("User1", user.getName());
		} finally {
			sqlSession.close();
		}
	}

//	@Test
//	public void shouldInsertACart() {
//		SqlSession sqlSession = sqlSessionFactory.openSession();
//		try {
//			Mapper mapper = sqlSession.getMapper(Mapper.class);
//			User user = new User();
//			user.setId(2);
//			user.setName("User2");
//			mapper.insertUser(user);
//		} finally {
//			sqlSession.close();
//		}
//	}

}
