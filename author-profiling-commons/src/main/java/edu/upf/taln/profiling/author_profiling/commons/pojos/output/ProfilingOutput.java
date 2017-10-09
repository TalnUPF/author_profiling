package edu.upf.taln.profiling.author_profiling.commons.pojos.output;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfilingOutput {
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Data{
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Dbpedia{
			
			@JsonIgnoreProperties(ignoreUnknown = true)
			public static class All{
				Integer end;
				String text;
				String type;
				String uri;
				Integer begin;
				
				public All(){
				}
				
				public int getEnd() {
					return end;
				}
				public void setEnd(int end) {
					this.end = end;
				}
				public String getText() {
					return text;
				}
				public void setText(String text) {
					this.text = text;
				}
				public String getType() {
					return type;
				}
				public void setType(String type) {
					this.type = type;
				}
				public String getUri() {
					return uri;
				}
				public void setUri(String uri) {
					this.uri = uri;
				}
				public int getBegin() {
					return begin;
				}
				public void setBegin(int begin) {
					this.begin = begin;
				}
				
				
			}
			
			List<All> all = new ArrayList<All>();
			
			public Dbpedia(){
			}

			public List<All> getAll() {
				return all;
			}

			public void setAll(List<All> all) {
				this.all = all;
			}
		}
		
		String original;
		String translation;
		Dbpedia dbpedia;
		
		public Data() {
		}
		
		public String getOriginal() {
			return original;
		}
		public void setOriginal(String original) {
			this.original = original;
		}
		public String getTranslation() {
			return translation;
		}
		public void setTranslation(String translation) {
			this.translation = translation;
		}

		public Dbpedia getDbpedia() {
			return dbpedia;
		}

		public void setDbpedia(Dbpedia dbpedia) {
			this.dbpedia = dbpedia;
		}
		
	}
    
    private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
    
}
