package com.linng.www.util;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.ScriptService.ScriptType;

/**
 * ES测试类
 * 
 * @author LiNing
 * 
 */
public class Test {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Client client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(
						"172.16.0.26", 9300));

		try {
			
			indexResponse(client);
			
			//getResponse(client);//取值API
			
			//DeleteResponse deleteResponse = client.prepareDelete("newindex", "test", "1").execute().actionGet();
			//System.out.println(deleteResponse.isFound());
			
			//updateRequest(client);
			
			//bulkRequest(client);
			
			//bulkProcessor(client);
			
			getResponse(client);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void bulkProcessor (Client client ) throws IOException {
		
		BulkProcessor processor = BulkProcessor.builder(client, new Listener() {
			
			@Override
			public void beforeBulk(long arg0, BulkRequest arg1) {
				System.out.println("beforeBulk");
			}
			
			@Override
			public void afterBulk(long arg0, BulkRequest arg1, Throwable arg2) {
				System.out.println(arg2.getMessage());
			}
			
			@Override
			public void afterBulk(long arg0, BulkRequest arg1, BulkResponse arg2) {
				System.out.println(arg2.hasFailures());
			}
		}).build();
		
		processor.add(new IndexRequest("newindex", "test", "1").source(
				jsonBuilder().startObject()
				.field("user", "Processor")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch").endObject()));
		
		processor.add(new DeleteRequest("newindex", "test", "2"));
		
		processor.close();
		
	} 
	
	public static void bulkRequest(Client client) throws Exception {
		BulkRequestBuilder builder = client.prepareBulk();
		
		builder.add(client.prepareIndex("newindex", "test", "1").setSource(
				jsonBuilder().startObject()
				.field("user", "Helo")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch").endObject()));
		
		builder.add(client.prepareIndex("newindex", "test", "2").setSource(
				jsonBuilder().startObject()
				.field("user", "Mill")
                .field("postDate", new Date())
                .field("message", "another post").endObject()));
		
		BulkResponse response = builder.execute().actionGet();
		if (response.hasFailures()) {
		}
		getResponse(client);
	}
	
	public static void indexResponse(Client client) throws Exception {
		IndexResponse response = client
		.prepareIndex("newindex", "test", "2")
		.setSource(jsonBuilder()
				.startObject()
				.field("user", "Helos")
				.field("postDate", new Date())
				.field("message", "another try")
				.endObject()).execute().actionGet();
		
		System.out.println(response.isCreated());
		
		// Index name
		String _index = response.getIndex();
		// Type name
		String _type = response.getType();
		// Document ID (generated or not)
		String _id = response.getId();
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();
		// isCreated() is true if the document is a new one, false if it has been updated
		boolean created = response.isCreated();
	} 
	
	public static void updateRequest (Client client) throws Exception {
		
		UpdateRequest request = new UpdateRequest("newindex", "test", "1");
		request.doc(jsonBuilder().startObject().field("age",15).endObject());//不存在的属性会 合并到文档中
		client.update(request).get();//注意调用get()，不然更新不会立即生效
		getResponse(client);
		
		client.prepareUpdate("newindex", "test", "1").setScript("ctx._source.age += 5", ScriptType.INLINE).get();
		getResponse(client);

		client.prepareUpdate("newindex", "test", "1").setDoc(jsonBuilder().startObject().field("gender" , "male").endObject()).get();
		getResponse(client);
	}
	
	public static void getResponse(Client client) {
		GetResponse getResponse = client.prepareGet("newindex", "test", "1").setOperationThreaded(false)
		        .execute()
		        .actionGet();
		System.out.println(getResponse.getId());
		Map<String , Object> map = getResponse.getSource();
		Set<Entry<String, Object>> entries = map.entrySet();
		if (entries != null) {
			Iterator<Entry<String, Object>> it = entries.iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		}
	}
}