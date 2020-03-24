'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const axios = require('axios');
import autosize from 'autosize';

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {recipes: []};
		this.updateRecipes = this.updateRecipes.bind(this);
		this.addRecipe = this.addRecipe.bind(this);
	}

	componentDidMount() {
		client({method: 'GET', path: '/recipes'}).done(response => {
			this.setState({recipes: response.entity});
		});
	}

	updateRecipes(data) {
        this.setState({recipes: data})
    }

    addRecipe(recipe) {
        axios.post('/recipes', {
            name: recipe.name,
            steps: recipe.steps,
            ingredients: recipe.ingredients})
        .then(response => {
            client({method: 'GET', path: '/recipes'}).done(response => {
                this.updateRecipes(response.entity);
            });
            $.notify("Recipe added", "success");
        }).catch(function (error) {
            $.notify("An error occurred", "error");
        });
    }

	render() {
	    return (
	        <div class="container-fluid">
	            <div class="row">
	                <AddRecipe addRecipe={this.addRecipe} update={this.updateRecipes}/>
	            </div>
	            <div class="row">
	                <RecipeList recipes={this.state.recipes} update={this.updateRecipes}/>
	            </div>
	        </div>
	    )
	}
}

class AddRecipe extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            id: '',
            name: '',
            ingredientType: '',
            unit: '',
            quantity: '',
            ingredients: [{ingredientType: '',  unit: '', quantity: ''}],
            step: '',
            steps: [{step: ''}],
        };
        this.baseState = this.state;
    }

    componentDidMount(){
        this.textarea.focus();
        autosize(this.textarea);
        $('#addRecipeModal').on('hidden.bs.modal', function () {
            $(this).find("input,textarea,select").val([]).end();
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        var recipe = {
            id: this.state.id,
            name: this.state.name,
            ingredients: this.state.ingredients,
            steps: this.state.steps
        };
        this.props.addRecipe(recipe);
    }

    handleClose(event) {
        this.setState(this.baseState);
    }

    handleNameChange(event) {
        const newName = event.target.value;
        this.setState({name: newName});
    }

    handleStepChange(event, i) {
        const newSteps = this.state.steps.map((recipeStep, idx) => {
            if (i !== idx) return recipeStep;
            return { ...recipeStep, step: event.target.value };
        });
        this.setState({ steps: newSteps });
    }

    handleIngredientChange(event, i) {
        const newSteps = this.state.ingredients.map((recipeStep, idx) => {
            if (i !== idx) return recipeStep;
            return { ...recipeStep, [event.target.name]: event.target.value };
        });
        this.setState({ ingredients: newSteps });
    }

    handleAddStep() {
        this.setState({
            steps: this.state.steps.concat([{ step: "" }])
        });
    }

    handleAddIngredient() {
        this.setState({
            ingredients: this.state.ingredients.concat([{ingredientType: '',  unit: '', quantity: ''}])
        });
    }

    handleRemoveStep(i) {
        this.setState({
              steps: this.state.steps.filter((step, idx) => i !== idx)
        });
    }

    handleRemoveIngredient(i) {
        this.setState({
            ingredients: this.state.ingredients.filter((step, idx) => i !== idx)
        });
    }

    render() {
        return (
            <React.Fragment>
                <div class="modal fade" id="addRecipeModal" tabindex="-1" role="dialog">
                    <div class="modal-dialog modal-xl" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Add recipe</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                        <div class="modal-body">
                            <div class="container-fluid">
                                <form>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="md-form">
                                                <input type="text" id="name-form" placeholder="Name" class="form-control" name="name" onChange={event => this.handleNameChange(event)}/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                        {this.state.steps.map((recipeStep, i) => (
                                            <div class="row">
                                                <div class="next-input col-md-12">
                                                    <div class="input-group mb-3">
                                                        <textarea key={i} type="text" placeholder={`Step #${i + 1}`} class="next-step form-control" aria-describedby="button-addon2" value={recipeStep.step} name="step"  ref={el => this.textarea = el} rows={1} defaultValue="" onChange={event => this.handleStepChange(event, i)}>
                                                        </textarea>
                                                        <div class="form-group-append">
                                                            <button class="btn btn-outline-secondary" type="button" id="button-addon2" onClick={() => this.handleRemoveStep(i)}>X</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        ))}
                                        <button type="button" class="btn btn-primary" onClick={() => this.handleAddStep()}>
                                            Add step
                                        </button>
                                        </div>
                                        <div class="col-md-6">
                                        {this.state.ingredients.map((recipeIngredient, i) => (
                                            <div class="next-input form-row">
                                                <div class="form-group col-md-2">
                                                    <input key={i} type="text" placeholder="Quantity" class="form-control" aria-describedby="button-addon2" value={recipeIngredient.quantity} name="quantity" onChange={event => this.handleIngredientChange(event, i)} />
                                                </div>
                                                <div class="form-group col-md-2">
                                                    <select key={i} class="form-control" value={recipeIngredient.unit} name="unit" onChange={event => this.handleIngredientChange(event, i)}>
                                                        <option selected>Unit</option>
                                                        <option value="PIECE">piece</option>
                                                        <option value="ML">ml</option>
                                                        <option value="L">l</option>
                                                        <option value="G">g</option>
                                                        <option value="KG">kg</option>
                                                        <option value="TSP">tsp</option>
                                                        <option value="TBSP">tbsp</option>
                                                        <option value="PINCH">pinch</option>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-7">
                                                    <input key={i} type="text" placeholder="Ingredient" class="form-control" aria-describedby="button-addon2" value={recipeIngredient.ingredientType} name="ingredientType" onChange={event => this.handleIngredientChange(event, i)} />
                                                </div>
                                                <div class="form-group-append">
                                                    <button class="btn btn-outline-secondary" type="button" id="button-addon2" onClick={() => this.handleRemoveIngredient(i)}>X</button>
                                                </div>
                                            </div>
                                        ))}
                                        <button type="button" class="btn btn-primary" onClick={() => this.handleAddIngredient()}>
                                            Add ingredient
                                        </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" onClick={event => this.handleClose(event)} data-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" onClick={event => this.handleSubmit(event)}>Save recipe</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-6">
                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target={"#addRecipeModal"}>
                        Add recipe
                    </button>
                </div>
            </React.Fragment>
        )
    }
}

class DeleteRecipeButton extends React.Component{

    deleteRecipe(id) {
        axios.delete('/recipes/' + id, {
        }).then(response => {
            client({method: 'GET', path: '/recipes'}).done(response => {
                this.props.update(response.entity);
            });
            $.notify("Recipe deleted.", "success");
        }).catch(function (error) {
            $.notify("An error occurred", "error");
        });
    }

    render() {
        return (
            <button type="button" class="btn btn-danger" onClick={() => { if (window.confirm('Are you sure you wish to delete this recipe?')) this.deleteRecipe(this.props.recipeId)}}>
                Delete
            </button>
        )
    }
}

class ShowDetailsButton extends React.Component{

    showIngredientsList() {
        let ingredientsList = [];
        var ingredients = this.props.recipe.ingredients;
        for (var i = 0; i < ingredients.length; i++) {
            var ingredient = ingredients[i];
            ingredientsList.push(
                <li class="list-group-item">
                    {ingredient.quantity} {ingredient.unit} <b>{ingredient.ingredientType}</b>
                </li>
            );
        }
        return ingredientsList;
    }

    showRecipeSteps() {
        let recipeSteps = [];
        var steps = this.props.recipe.steps;
        for (var i = 0; i < steps.length; i++) {
            var step = steps[i];
            recipeSteps.push(
                <li class="list-group-item">
                    <b>{i + 1}</b>. {step.step}
                </li>
            );
        }
        return recipeSteps;
    }

    render() {
        return (
            <React.Fragment>
                <div class="modal fade" id={"showDetailsModal" + this.props.recipe.id} tabindex="-1" role="dialog">
                  <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title">{this.props.recipe.name}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                          <span aria-hidden="true">&times;</span>
                        </button>
                      </div>
                      <div class="modal-body">
                        <div class="container">
                            <div class="row">
                                <div class="col-5">
                                    <ul class="list-group list-group-flush">
                                        {this.showIngredientsList()}
                                    </ul>
                                </div>
                                <div class="col">
                                    <ul class="list-group list-group-flush">
                                        {this.showRecipeSteps()}
                                    </ul>
                                </div>
                            </div>
                        </div>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                      </div>
                    </div>
                  </div>
                </div>
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target={"#showDetailsModal" + this.props.recipe.id}>
                    Show recipe
                </button>
            </React.Fragment>
        )
    }
}

class RecipeList extends React.Component{

	render() {
		const recipes = this.props.recipes.map(r =>
			<Recipe key={r.id} recipe={r} update={this.props.update}/>
		);
		return (
		<div class="col-6">
			<table id="main" class="table table-hover">
				<thead class="thead-dark">
				    <tr>
                        <th>Recipe</th>
                		<th></th>
                	</tr>
				</thead>
				<tbody>
					{recipes}
				</tbody>
			</table>
		</div>
		)
	}
}

class Recipe extends React.Component{

	render() {
		return (
			<tr>
				<td>{this.props.recipe.name}</td>
				<td>
				<ShowDetailsButton recipe={this.props.recipe} />
				{' '}
				<DeleteRecipeButton recipeId={this.props.recipe.id} update={this.props.update}/>
				</td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
