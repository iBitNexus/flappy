package com.pleisto;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

public class SandboxTest {

    @Test
    public void ping() {
        assertThat(FlappyJniSandbox.ping()).isEqualTo("pong");
    }

    @Test
    public void evalFail() throws InterruptedException {
        final FlappyJniSandbox op = new FlappyJniSandbox();
        try {
            FlappyJniSandboxResult result =
                    op.evalPythonCode(new FlappyJniSandboxInput("foobar")).get();
            assertThat(false).isTrue();
        } catch (ExecutionException e) {
            assertThat(true).isTrue();
        }
    }

    @Test
    public void evalSuccess() throws ExecutionException, InterruptedException {
        final FlappyJniSandbox op = new FlappyJniSandbox();
        String code = "print('hello world')";
        FlappyJniSandboxResult result =
                op.evalPythonCode(new FlappyJniSandboxInput(code)).get();
        assertThat(result).isNotNull();
        System.out.println(result);
        assertThat(result.stdout).isEqualTo("hello world\n");
    }

    @Test
    public void echo() {
        assertThat(FlappyJniDummy.echo("foo", true, new HashMap<>(), "bar"))
                .isEqualTo("code: foo, network: true, envs: [], cache_path: Some(\"bar\")");
        assertThat(FlappyJniDummy.echo("foo", false, Collections.singletonMap("A", "32"), ""))
                .isEqualTo("code: foo, network: false, envs: [(\"A\", \"32\")], cache_path: None");
    }
}
