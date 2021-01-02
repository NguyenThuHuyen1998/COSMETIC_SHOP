//package com.example.crud.crud;
//
//
//import com.example.crud.entity.User;
//import com.sun.net.httpserver.Authenticator;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.TestName;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.xml.transform.Result;
//
//@SpringBootTest
//class CrudApplicationTests {
//
//    @Test
//    void contextLoads() {
//
//    }
//
//
//    @Test
//    void testCategory(){
//        String str = "Junit is working fine";
//        assertEquals("Junit is working fine",str);
//    }
//
//    private void assertEquals(String junit_is_working_fine, String str) {
//    }
//
//    @Test
//    public void whenFindByName_thenReturnEmployee() {
//        // given
//        User alex = new User();
//        entityManager.persist(alex);
//        entityManager.flush();
//
//        // when
//        Employee found = employeeRepository.findByName(alex.getName());
//
//        // then
//        assertThat(found.getName())
//                .isEqualTo(alex.getName());
//    }
//
//}
//
