<script>

	import HealthCheck from "./HealthCheck.svelte";

	const appUrl = 'http://jdk17-spring-client-noahmiller-dev.apps.sandbox.x8i5.p1.openshiftapps.com'

	let operations = [
		{ id: 1, text: "Simplify"},
		{ id: 2, text: "Factor"},
		{ id: 3, text: "Derive"},
		{ id: 4, text: "Integrate"},
		{ id: 5, text: "Zeroes"},
		{ id: 6, text: "Tangent"},
		{ id: 7, text: "Area"},
	];

	let healthResult = '';

	let selected;

	let expression = '';

	let results = [];

	function handleSubmit() {
		getAnswer()
	}

	function addResult(result) {
		results.push(result)
		results = results
	}

	function resetHealthResult() {
		healthResult = ''
	}
	
	async function getAnswer() {
		console.log(results.length)
		let operation = selected.text
		try {
			const res = await fetch(appUrl + '/calculate', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({operation: operation.toLowerCase(), expression})
			});
			if (!res.ok) {
				addResult({operation, expression, result: "FAILURE"})
				return
			} 
			const json = await res.json();
			console.log(json);
			addResult({...json, operation});
			resetHealthResult();
		} catch(error){
			addResult({operation, expression, result: "FAILURE"})
		}
	}
</script>

<div class="calculator">
	<h2>Expression Calculator</h2>

	<form on:submit|preventDefault={handleSubmit}>
		<select bind:value={selected}>
			{#each operations as operation}
				<option value={operation}>
					{operation.text}
				</option>
			{/each}
		</select>
		<input bind:value={expression}>
		<button disabled={!expression} type=submit>
			Submit
		</button>
	</form>

	<HealthCheck bind:healthResult />
</div>

{#if results.length > 0}
	<h2>Results</h2>
	<table>
		<thead>
			<tr>
				<th>Operation</th>
				<th>Expression</th>
				<th>Result</th>
			</tr>
		</thead>
		<tbody>
			{#each results as {operation, expression, result}}
			<tr>
				<td>{operation}</td>
				<td>{expression}</td>
				<td>{result}</td>
			</tr>
		{/each}
		</tbody>
	</table>
{/if}


<style>
	.calculator {
		background-color: gainsboro;
		padding: 15px 30px;
		margin: 5px 30px;
		display: inline-block;
		border-radius: 10px;
	}

	.calculator h2 {
		margin: 10px
	}

	table, th, td {
		border: 1px solid;
		border-collapse: collapse;
		margin-bottom: 10px;
	}

	table {
		border: 1px solid #1C6EA4;
		background-color: #EEEEEE;
		width: 100%;
		text-align: left;
		border-collapse: collapse;
	}
	table td, table.blueTable th {
		border: 1px solid #AAAAAA;
		padding: 3px 2px;
	}
	table tbody td {
		font-size: 13px;
	}
	table tr:nth-child(even) {
		background: #D0E4F5;
	}
	table thead {
		background: #1C6EA4;
		background: -moz-linear-gradient(top, #5592bb 0%, #327cad 66%, #1C6EA4 100%);
		background: -webkit-linear-gradient(top, #5592bb 0%, #327cad 66%, #1C6EA4 100%);
		background: linear-gradient(to bottom, #5592bb 0%, #327cad 66%, #1C6EA4 100%);
		border-bottom: 2px solid #444444;
	}
	table thead th {
		font-size: 15px;
		font-weight: bold;
		color: #FFFFFF;
		border-left: 2px solid #D0E4F5;
	}
	table thead th:first-child {
		border-left: none;
	}
</style>