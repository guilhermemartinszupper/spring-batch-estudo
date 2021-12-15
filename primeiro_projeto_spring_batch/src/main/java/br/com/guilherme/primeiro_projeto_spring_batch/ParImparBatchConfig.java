package br.com.guilherme.primeiro_projeto_spring_batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@EnableBatchProcessing
@Configuration
public class ParImparBatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job imprimeParImparJob(){
        return jobBuilderFactory
                .get("ParImparJob")
                .start(imprimeParImparStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step imprimeParImparStep() {
        return stepBuilderFactory
                .get("ImprimeParImparStep")
                .<Integer, String>chunk(10)
                .reader(contaAteDezReader())
                .processor(parOuImparProcessor())
                .writer(imprimeWriter())
                .build();
    }

    public ItemWriter<String> imprimeWriter() {
        return itens -> itens.forEach(System.out::println);
    }

    public FunctionItemProcessor<Integer, String> parOuImparProcessor() {
        return new FunctionItemProcessor<>(item -> item % 2 == 0 ? String.format("Item %s é Par",item) : String.format("Item %s é Impar",item));
    }

    public IteratorItemReader<Integer> contaAteDezReader() {
        List<Integer> numerosDeUmAteDez = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        return new IteratorItemReader<Integer>(numerosDeUmAteDez.iterator());
    }
}
