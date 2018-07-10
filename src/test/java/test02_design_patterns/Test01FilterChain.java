package test02_design_patterns;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试 责任链模式 
 *
 */
public class Test01FilterChain {
	
	public static void main(String[] args) {
		Request request = new Request("request");
		Response response = new Response("response");
		
		FilterChain chain = new FilterChain();
		chain.addFilter(new Filter1()).addFilter(new Filter2()).addFilter(new Filter3());
		chain.doFilter(request, response, chain);
		
		System.out.println(request.getContent());
		System.out.println(response.getContent());
	}
	
	/**
	 * 请求体
	 * 
	 */
	static class Request {
		private String content;
		public Request(String content) {
			this.content = content;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
	/**
	 * 响应体
	 * 
	 */
	static class Response {
		private String content;
		public Response(String content) {
			this.content = content;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
	/**
	 * 过滤器
	 * 
	 */
	static interface Filter {
		void doFilter(Request request, Response response, FilterChain chain);
	}
	/**
	 * 过滤器链
	 * 
	 */
	static class FilterChain implements Filter {
		private List<Filter> filters = new ArrayList<Filter>();
		private int i = 0;

		public List<Filter> getFilters() {
			return filters;
		}
		public void setFilters(List<Filter> filters) {
			this.filters = filters;
		}
		public FilterChain addFilter(Filter filter) {
			this.filters.add(filter);
			return this;
		}
		
		@Override
		public void doFilter(Request request, Response response, FilterChain chain) {
			if (chain.getFilters().isEmpty() || i==chain.getFilters().size()) {
				return;
			}
			Filter filter = chain.getFilters().get(i);
			i++;
			filter.doFilter(request, response, chain);
		}
	}
	/**
	 * 过滤器 1 2 3
	 * 
	 */
	static class Filter1 implements Filter {
		@Override
		public void doFilter(Request request, Response response, FilterChain chain) {
			request.setContent(request.getContent() + " filter1");
			chain.doFilter(request, response, chain);
			response.setContent(response.getContent() + " filter1");
		}
	}
	static class Filter2 implements Filter {
		@Override
		public void doFilter(Request request, Response response, FilterChain chain) {
			request.setContent(request.getContent() + " filter2");
			chain.doFilter(request, response, chain);
			response.setContent(response.getContent() + " filter2");
		}
	}
	static class Filter3 implements Filter {
		@Override
		public void doFilter(Request request, Response response, FilterChain chain) {
			request.setContent(request.getContent() + " filter3");
			chain.doFilter(request, response, chain);
			response.setContent(response.getContent() + " filter3");
		}
	}
}
