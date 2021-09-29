<script>
	let operations = [
		{ id: 1, text: "Simplify"},
		{ id: 2, text: "Factor"},
		{ id: 3, text: "Derive"},
		{ id: 4, text: "Integrate"},
		{ id: 5, text: "Zeroes"}
	];

	let selected;
	let promise;
	let answered = false;

	let expression = '';

	function handleSubmit() {
		answered = true
		promise = getAnswer()
	}
	
	async function getAnswer() {
		let operation = selected.text.toLowerCase()
		const res = await fetch('http://localhost:8080/calculate', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({operation, expression})
		})
		const json = await res.json();
		console.log(json);
		return json;
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

{#if answered}
	{#await promise then expressionResult}
		<div>
			Result: {expressionResult.result}	
		</div>
	{:catch error}
		<div class="error">
			Error: {error.message}
		</div>
	{/await}
{/if}
