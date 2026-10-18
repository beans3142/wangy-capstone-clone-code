package com.wangyu;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicMapperTest {

    private final BasicMapper mapper = new BasicMapper() {
    };

    @Test
    public void convertToDtoMapsMatchingFieldsByName() {
        SourceFixture source = new SourceFixture("wangyu", 10);

        TargetFixture target = mapper.convertToDto(source, TargetFixture.class);

        assertEquals("wangyu", target.getName());
        assertEquals(10, target.getAge());
    }

    @Test
    public void convertToDtoListMapsEachElement() {
        List<SourceFixture> sources = List.of(
                new SourceFixture("a", 1),
                new SourceFixture("b", 2));

        List<TargetFixture> targets = mapper.convertToDtoList(sources, TargetFixture.class);

        assertEquals(2, targets.size());
        assertEquals("a", targets.get(0).getName());
        assertEquals("b", targets.get(1).getName());
    }

    public static class SourceFixture {
        private String name;
        private int age;

        public SourceFixture(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    public static class TargetFixture {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
