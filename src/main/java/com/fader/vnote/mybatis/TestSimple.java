package com.fader.vnote.mybatis;

import com.fader.vnote.mybatis.mapper.ScoreMapper;
import com.fader.vnote.mybatis.mapper.StudentsMapper;
import com.fader.vnote.mybatis.model.Score;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author FaderW
 * 2019/11/25
 */

public class TestSimple {

    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        sqlSession.selectOne("select * from students");
//        sqlSession.select();
//        StudentsMapper mapper = sqlSession.getMapper(StudentsMapper.class);
//        System.out.println(mapper.selectStu("20190524"));
//        System.out.println(mapper.selectStu("20190524"));
//        System.out.println(mapper.selectStu("20190524"));
        ScoreMapper scoreMapper = sqlSession.getMapper(ScoreMapper.class);
//        Score score = scoreMapper.getScoreById("1");
//        System.out.println(score.getId());
//        System.out.println(score.getStuId());
//
//        System.out.println(score.getStudents());
    }

    public static synchronized void test() {
        System.out.println("test");
    }

//    public static void main(String[] args) {
//        int oldCapacity = Integer.MAX_VALUE - 16;
//        System.out.println(oldCapacity);
//        int minCapacity = Integer.MAX_VALUE - 15;
//        int maxSize = Integer.MAX_VALUE - 8;
//        int newCapacity = oldCapacity + (oldCapacity >> 1);
//
//        if (newCapacity - minCapacity < 0)
//            newCapacity = minCapacity;
//        if (newCapacity - maxSize > 0)
//            newCapacity = hugeCapacity(minCapacity);
//        // minCapacity is usually close to size, so this is a win:
//        System.out.println(newCapacity);
//    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > Integer.MAX_VALUE - 8) ?
                Integer.MAX_VALUE :
                Integer.MAX_VALUE - 8;
    }
}
