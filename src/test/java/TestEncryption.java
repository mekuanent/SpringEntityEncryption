import com.github.mekuanent.encryption.handler.PBEHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Created by Mekuanent Kassaye on 2019-07-02.
 */

public class TestEncryption {


    @Test
    public void testPBEEncryption(){

        PBEHandler handler = new PBEHandler(new char[]{'H', 'E', 'L', 'L', 'O', 'M', 'Y', 'P', 'A', 'S', 'S'},
                "some really huge salt".getBytes(), "akjhsdjalshdkasjhdkjashdksad".getBytes(), 100, 256);

        String text = "Some Text";
        String cipher = handler.encrypt(text);
        String raw = handler.decrypt(cipher);

        Assert.assertEquals(text, raw);

    }


}
