<script>
	let operations = [
		{ id: 1, text: "Simplify"},
		{ id: 2, text: "Factor"},
		{ id: 3, text: "Derive"},
		{ id: 4, text: "Integrate"},
		{ id: 5, text: "Zeroes"}
	];

	let selected;

	let expression = '';

	let promise;
	let answered = false;
	let results = [];

	function handleSubmit() {
		answered = true
		promise = getAnswer()
	}

	function addResult(result) {
		results.push(result)
		results = results
	}
	
	async function getAnswer() {
		console.log(results.length)
		let operation = selected.text
		try {
			const res = await fetch('http://localhost:8080/calculate', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({operation: operation.toLowerCase(), expression})
			});
			const json = await res.json();
			console.log(json);
			addResult({...json, operation});
		} catch(error){
			addResult({operation, expression, result: "FAILURE"})
		}
	}
</script>

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
