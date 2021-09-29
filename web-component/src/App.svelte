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

<HealthCheck bind:healthResult />

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

{#if results.length > 0}
	<h2>Results</h2>
	<table>
		<tr>
			<th>Operation</th>
			<th>Expression</th>
			<th>Result</th>
		</tr>
		{#each results as {operation, expression, result}}
			<tr>
				<td>{operation}</td>
				<td>{expression}</td>
				<td>{result}</td>
			</tr>
		{/each}
	</table>
{/if}
