package application.models;

import org.junit.jupiter.api.Test;

class TitleTextTest {

    // todo: maybe add test ?

    @Test
    void parse() {

        var text = "# Hello world!\n" +
                "Hello java spring boot\n" +
                "\n" +
                "# Article Lorem ipsum\n" +
                "hello world today is the day\n" +
                "time: 19:48 01.03.2022\n" +
                "## Subheader\n" +
                "some details ....\n" +
                "etc.";
        var parsed = TitleText.parse(text);

    }

}