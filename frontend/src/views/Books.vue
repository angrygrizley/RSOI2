<template>
    <div class="games">
        <h1>Книги</h1>
        <div class="games-list" v-bind:key="game.id" v-for="game in games">
            <GameItem  v-bind:game="game"/>
        </div>
    </div>
</template>

<script>
    import GameItem from '../components/GameItem';
    import axios from 'axios';
    export default {
        name: "games",
        components:{GameItem},
        data(){
            // return{
            //     games:[{id:"1", name:"game", description:"blablalba"}]
            // }
            return{
                games:[]
            }
        },
        created() {
            axios.get("/api/games")
                .then(res => this.games = res.data)
                .catch(err => console.log(err))
        }
    }

</script>

<style scoped>
    .games-list{
        width: 60%;
        margin: auto;
        padding: 8px;
    }
</style>