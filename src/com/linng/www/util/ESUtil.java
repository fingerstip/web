package com.linng.www.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.optimize.OptimizeResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import com.google.common.collect.Lists;
import com.linng.www.vo.Account;

public class ESUtil {

	public static Page<Account> searchList(Page<Account> page, Client client) {

		SearchRequestBuilder searchRequestBuilder = getSearchRequestBuilder(
				client).setQuery(getQuery(page)).setFrom(page.getFirst())
				.setSize(page.getPageSize())
				.addSort("account_number", SortOrder.DESC);

		setHighlightedField(page, searchRequestBuilder);

		SearchResponse searchResponse = searchRequestBuilder.execute()
				.actionGet();

		page.setResult(getBeans(searchResponse, page));
		page.setTotalCount(getTotalCount(searchResponse));

		return page;
	}

	private static List<Account> getBeans(SearchResponse searchResponse,
			Page<Account> page) {

		String address = (String) page.getParams().get("address");
		String firstName = (String) page.getParams().get("firstname");

		List<Account> list = Lists.newArrayList();
		// 获取结果
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();

		for (SearchHit hit : searchHits) {
			Account account = makeAccount(hit);
			if (StringUtils.isBlank(address) && StringUtils.isBlank(firstName)) {
				list.add(account);
			} else {
				list.add(setHighlightedField(account, hit));
			}
		}
		return list;
	}

	private static Account setHighlightedField(Account account, SearchHit hit) {
		HighlightField highlightField = hit.getHighlightFields().get("address");

		if (highlightField != null) {
			account.setAddress(highlightField.fragments()[0].toString());
		}

		highlightField = hit.getHighlightFields().get("firstname");
		if (highlightField != null) {
			account.setFirstname(highlightField.fragments()[0].toString());
		}

		highlightField = hit.getHighlightFields().get("lastname");
		if (highlightField != null) {
			account.setLastname(highlightField.fragments()[0].toString());
		}

		return account;
	}

	private static int getTotalCount(SearchResponse response) {
		return (int) response.getHits().getTotalHits();
	}

	private static SearchRequestBuilder setHighlightedField(Page<Account> page,
			SearchRequestBuilder searchRequestBuilder) {

		String address = (String) page.getParams().get("address");
		String firstName = (String) page.getParams().get("firstname");

		if (StringUtils.isNotBlank(address)) {
			searchRequestBuilder.addHighlightedField("address")
					.setHighlighterPreTags("<font color='red'>")
					.setHighlighterPostTags("</font>");
		}
		if (StringUtils.isNotBlank(firstName)) {
			searchRequestBuilder.addHighlightedField("firstname")
					.addHighlightedField("lastname")
					.setHighlighterPreTags("<font color='red'>")
					.setHighlighterPostTags("</font>");
		}

		return searchRequestBuilder;
	}

	public static Account get(String accountNumber, Client client) {
		QueryBuilder q = QueryBuilders.matchPhraseQuery("account_number",
				accountNumber);

		SearchResponse searchResponse = getSearchRequestBuilder(client)
				.setQuery(q).execute().actionGet();

		SearchHits hits = searchResponse.getHits();
		System.out.println("查询记录数 :" + hits.getTotalHits());
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			Account account = makeAccount(hit);
			return account;
		}
		return null;
	}

	private static Account makeAccount(SearchHit hit) {

		Integer id = Integer.valueOf(hit.getId());
		Integer accountNumber = (Integer) hit.getSource().get("account_number");
		Integer balance = (Integer) hit.getSource().get("balance");
		String firstName = (String) hit.getSource().get("firstname");
		String lastName = (String) hit.getSource().get("lastname");
		Integer age = (Integer) hit.getSource().get("age");
		String gender = (String) hit.getSource().get("gender");
		String address = (String) hit.getSource().get("address");
		String employer = (String) hit.getSource().get("employer");
		String email = (String) hit.getSource().get("email");
		String city = (String) hit.getSource().get("city");
		String state = (String) hit.getSource().get("state");

		Account account = new Account(id, accountNumber, balance, firstName,
				lastName, age, gender, address, employer, email, city, state);
		return account;

	}

	public static Integer getMaxId(Client client) {
		QueryBuilder q = QueryBuilders.matchAllQuery();
		SearchResponse searchResponse = getSearchRequestBuilder(client)
				.setQuery(q).addSort("account_number", SortOrder.DESC)
				.setSize(1).execute().actionGet();

		Integer result = -1;
		// 获取结果
		SearchHits hits = searchResponse.getHits();

		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			result = Integer.parseInt(hit.getId());
		}

		return result;

	}

	public static void delete(String[] ids, Client client) {
		// Client c = getClient();
		BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

		for (String id : ids) {
			DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete()
					.setIndex(Constants.INDEX).setType(Constants.TYPE)
					.setId(id);
			bulkRequestBuilder.add(deleteRequestBuilder);
		}

		BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			System.out.println(bulkResponse.buildFailureMessage());
		}

		optimize(client);
	}

	public static boolean save(Account account, Client client)
			throws ElasticsearchException, Exception {

		IndexRequestBuilder requestBuilder = client.prepareIndex(
				Constants.INDEX, Constants.TYPE).setRefresh(true);

		// 是否新增
		if (account.getAccount_number() == null || account.getId() == null) {
			Integer maxId = getMaxId(client);

			account.setId(maxId + 1);
			account.setAccount_number(maxId + 1);
		}

		// JSONObject json = new JSONObject(account);
		String accountJson = obj2JsonData(account);

		IndexResponse response = requestBuilder
				.setId(String.valueOf(account.getId())).setSource(accountJson)
				.execute().actionGet();

		boolean flag = response.isCreated();// 返回true标识新增，如为false，表明更新
		return flag;

	}

	/**
	 * 获取传入对象的私有属性，并返回JSON
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String obj2JsonData(Object obj) {

		String jsonData = null;

		XContentBuilder builder;
		try {
			builder = XContentFactory.jsonBuilder();
			XContentBuilder xContentBuilder = builder.startObject();
			Field[] fields = obj.getClass().getDeclaredFields();
			// 在使用java反射机制获取 JavaBean 的属性值时，如果该属性被声明为private
			// 的，需要将setAccessible设置为true.
			Field.setAccessible(fields, true);
			for (Field field : fields) {
				// 26 表明属性用 private static final 修饰 1表示 public 修饰
				if (field.getModifiers() == 1 || field.getModifiers() == 26) {
					continue;
				}
				xContentBuilder.field(field.getName(), field.get(obj));
			}
			xContentBuilder.endObject();

			jsonData = builder.string();
			System.out.println(jsonData);

		} catch (IOException | IllegalArgumentException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return jsonData;

	}

	/**
	 * 删除文档在Lucene中删除文档，数据不会马上进行硬盘上除去，而进在lucene索引中产生一个.del的文件，而在检索过程中这部分数据也会参与检索
	 * ， lucene在检索过程会判断是否删除了，如果删除了在过滤掉。这样也会降低检索效率。所以可以执行清除删除文档。 curl -XPOST
	 * ‘http://localhost:9300/XXXX/_optimize? only_expunge_deletes =true
	 * 
	 * */
	protected static OptimizeResponse optimize(Client client) {
		OptimizeResponse actionGet = client.admin().indices().prepareOptimize()
				.execute().actionGet();
		return actionGet;
	}

	public static Page<Account> search(Page<Account> page, Client client) {
		return searchList(page, client);
	}

	private static QueryBuilder getQuery(Page<Account> page) {

		BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();

		String gteAge = (String) page.getParams().get("gteAge");
		String lteAge = (String) page.getParams().get("lteAge");
		String address = (String) page.getParams().get("address");
		String firstName = (String) page.getParams().get("firstname");

		if (StringUtils.isBlank(address) && StringUtils.isBlank(firstName)
				&& StringUtils.isBlank(gteAge) && StringUtils.isBlank(lteAge)) {
			return QueryBuilders.matchAllQuery();
		}

		if (StringUtils.isNotBlank(address)) {
			booleanQuery.must(QueryBuilders.matchQuery("address", address));
		}
		if (StringUtils.isNotBlank(firstName)) {
			booleanQuery.should(
					QueryBuilders.matchQuery("firstname", firstName)).should(
					QueryBuilders.matchQuery("lastname", firstName));
		}

		if (StringUtils.isNumeric(gteAge) && StringUtils.isNumeric(lteAge)) {
			booleanQuery.must(QueryBuilders.filteredQuery(
					QueryBuilders.matchAllQuery(),
					FilterBuilders.rangeFilter("age")
							.gte(Integer.parseInt(gteAge))
							.lte(Integer.parseInt(lteAge))));
		}

		return booleanQuery;

	}

	private static SearchRequestBuilder getSearchRequestBuilder(Client client) {
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(
				Constants.INDEX).setTypes(Constants.TYPE);
		return searchRequestBuilder;
	}

	public static void main(String[] args) {
		// 运行一次会创建一个ES实例 并加入到指定的clusterName中

		Node node = NodeBuilder.nodeBuilder().clusterName("elasticsearch")
				.node();
		Client client = node.client();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(
				Constants.INDEX).setTypes(Constants.TYPE);

		SearchResponse searchResponse = searchRequestBuilder
				.setQuery(QueryBuilders.matchAllQuery()).setFrom(1000)
				.setSize(20).addSort("account_number", SortOrder.DESC)
				.execute().actionGet();

		// 获取结果
		SearchHits hits = searchResponse.getHits();
		System.out.println("查询记录数 :" + hits.getTotalHits());

		SearchHit[] searchHits = hits.getHits();
		if (searchHits.length > 0) {
			System.out.println("searchHits.length :" + searchHits.length);
			for (SearchHit hit : searchHits) {
				System.out.println(hit.getSource().get("firstname"));
			}
		}
		client.close();

		System.out.println(client);
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * // 查询条件 QueryBuilder q = // QueryBuilders.matchAllQuery();
	 * 
	 * // QueryBuilders.matchQuery("address", "mill lane"); //
	 * QueryBuilders.matchPhraseQuery("address", "mill lane"); //
	 * QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("age", // "30"));
	 * // QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()); //
	 * QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), //
	 * FilterBuilders.rangeFilter("balance").gte(20000).lte(30000)); //
	 * QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("age", //
	 * "40")).must(QueryBuilders.matchQuery("state", "ID"));
	 * 
	 * QueryBuilders.matchPhraseQuery("address", "mill lane");
	 * 
	 * HighlightBuilder highlightBuilder = new HighlightBuilder();
	 * highlightBuilder.field("address"); List<Account> result =
	 * searchList(getClient(), q, "bank", "account"); for (Account account :
	 * result) { System.out.println(account.toString()); }
	 * 
	 * // agg();
	 * 
	 * }
	 */

	// public static void agg() {
	// SearchResponse searchResponse = client
	// .prepareSearch("bank")
	// .setTypes("account")
	// //
	// .addAggregation(AggregationBuilders.terms("by_state").field("state")).execute().actionGet();
	// //
	// .addAggregation(AggregationBuilders.terms("by_state").field("state").subAggregation(AggregationBuilders.avg("avg").field("balance"))).execute().actionGet();
	//
	// .addAggregation(
	// AggregationBuilders
	// .range("by_age")
	// .field("age")
	// .addRange(20, 30)
	// .addRange(30, 40)
	// .addRange(40, 50)
	// .subAggregation(
	// AggregationBuilders.terms(
	// "group_by_gender").field(
	// "gender"))
	// .subAggregation(
	// AggregationBuilders.avg("avg_tst")
	// .field("balance"))).execute()
	// .actionGet();
	//
	// // System.out.println("length = " +
	// // searchResponse.getHits().hits().length);
	// // Terms terms = searchResponse.getAggregations().get("by_age");
	// /*
	// * for (Bucket b : terms.getBuckets()) { System.out.println("key = " +
	// * b.getKey() + ", docCount = " + b.getDocCount());
	// *
	// * InternalAvg ia = b.getAggregations().get("avg");
	// * System.out.println(ia.getValue());
	// *
	// * }
	// */
	//
	// // searchResponse.getAggregations().asList().size();
	// System.out.println(searchResponse.getAggregations().get("by_age"));
	//
	// InternalRange<Bucket> ir = searchResponse.getAggregations().get(
	// "by_age");
	//
	// System.out.println("\"数据长度 :\"" + ir.getBuckets().size());
	//
	// Iterator<Bucket> it = ir.getBuckets().iterator();
	//
	// while (it.hasNext()) {
	//
	// Bucket bucket = it.next();
	//
	// System.out.println("key = " + bucket.getKey() + " , count="
	// + bucket.getDocCount());
	//
	// // Terms terms = bucket.getAggregations().get("group_by_gender");
	//
	// StringTerms terms = bucket.getAggregations().get("group_by_gender");
	//
	// for (int i = 0; i < terms.getBuckets().size(); i++) {
	// org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket b =
	// (org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket)
	// terms
	// .getBuckets().get(i);
	// System.out.println(b.getKey());
	// System.out.println(b.getDocCount());
	// }
	//
	// System.out.println();
	//
	// for (org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket b :
	// terms
	// .getBuckets()) {
	// System.out.println(b.getKey());
	// System.out.println(b.getDocCount());
	//
	// InternalAvg ia = b.getAggregations().get("avg_tst");
	//
	// System.out.println(ia);// TODO 不存在值 需要解决 骚年
	// // System.out.println("avg = " + ia.getValue());
	//
	// }
	//
	// }
	// }

}
