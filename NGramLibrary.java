
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class NGramLibrary{
	public static class MapperNGram extends Mapper<LongWritable, Text, Text,IntWritable>{
		int wordLen ;
		@Override
		public void setup(Context context){
			Configuration conf = context.getConfiguration();
			wordLen = conf.getInt("wordLen",4);
		}

		@Override
		//read dataset 
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
			String line = value.toString();
			//trim 
			line = line.trim().toLowerCase();
			//delete unuseful info
			line = line.replaceAll("[^a-z]"," ");
			//regardless of number of spaces
			String[] words = line.split("\\s+");

			if(words.length<2){
				return;
			}

			StringBuilder sb;
			for(int i = 0; i<words.length-1;i++){
				sb = new StringBuilder();
				sb.append(word);
				for(int j =1; i+j<words.length && j<wordLen;j++){
					sb.append(" ");
					sb.append(words[i+j]);
					context.write(new Text(sb.toString().trim()),new IntWritable(1));
				}

							}

		}

	}
	public static class ReducerNGram extends Reducer<Text, IntWritable, Text, IntWritable>{
		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) thro IOException, InterruptedException{
			//reduce count 
			int sum = 0;
			for(IntWritable value:values){
				sum += value.get();
			}
			context.write(key, new IntWritable(sum));
		}

	}
}