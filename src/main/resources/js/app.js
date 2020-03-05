'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {recipes: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/recipes'}).done(response => {
			this.setState({recipes: response.entity});
		});
	}

	render() {
		return (
			<RecipeList recipes={this.state.recipes}/>
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
                    {ingredient.quantity} {ingredient.unit} <b>{ingredient.ingredientType.type}</b>
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
			<Recipe key={r.id} recipe={r}/>
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
				<td><ShowDetailsButton recipe={this.props.recipe} /></td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
