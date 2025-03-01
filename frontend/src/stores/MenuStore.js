import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";

export const UseMenuStore = defineStore("menu",{
    state:() => ({
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        menus: [],
        menuItems: [],
        errors: '',
        isLoading: true,
        success: ''
    }),
    getters: {
        getMenus(){
            return this.menus;
        },
        getMenuItems(){
            return this.menuItems;
        }
    },
    actions: {
        async getToken(){
            await axios.get('/sanctum/csrf-cookie');
        },
        async fetchMenus(page = 1, search = '') {
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('menu?page=' + page,{
                    params: {
                        search: search
                    }
                });
                console.log(response.data.data)
                this.menus = response.data.data;
            } catch (error) {
                if (error.response.status === 422) {
                    this.errors.value = error.response.data.errors;
                }
            }finally {
                this.isLoading = false;
            }
        },
        async fetchMenuItems(page = 1, search = '', $id){
            this.menuItems = [];
            this.isLoading = true;
            await this.getToken();
            try{
                const response = await axios.get('menuItems/'+$id+'/items?page=' + page,{
                    params: {
                        search: search
                    }
                });
                console.log(response.data)
                this.menuItems = response.data.data;
            } catch (error) {
                if (error.response.status === 422) {
                    this.errors.value = error.response.data.errors;
                }
            }finally {
                this.isLoading = false;
            }
        },
        async insertMenu(name, description){
            try{
                const response = await axios.post('menu',{
                    name: name,
                    description: description
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenus();
            }catch (error) {
                if(error.response.status === 422){
                    this.errors = error.response.data.errors;
                }
            }
        },
        async updateMenu(menu){
            try{
                const response = await axios.put("menu/" + menu.id,{
                    name: menu.name,
                    description: menu.description
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenus();
            }catch (error) {
                if(error.response.status === 422){
                    this.errors = error.response.data.errors;
                }
            }
        },
        async destroyMenu(id){
            try{
                const response = await axios.delete("menu/" + id);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenus();
            }catch (error) {
                console.log(error)
            }
        },
        async insertMenuItem(id,name, short_description, long_description, recipe, picture, price){
            try{
                const response = await axios.post('menuItems/'+id+'/items',{
                        name: name,
                        short_description: short_description,
                        long_description: long_description,
                        recipe: recipe,
                        picture: picture,
                        price: price,
                    });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenuItems(id);
            }catch (error) {
                console.log(error)
            }
        },
        async updateMenuItem(id,menuItem){
            try{
                const response = await axios.put("menuItems/" + id +"/items/"+menuItem.id,{
                        name: menuItem.name,
                        short_description: menuItem.short_description,
                        long_description: menuItem.long_description,
                        recipe: menuItem.recipe,
                        picture: menuItem.picture,
                        price: menuItem.price,
                    });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenuItems(id);
            }catch (error) {
                console.log(error)
            }
        },
        async destroyMenuItem(id,menuItem){
            try{
                const response = await axios.delete("menuItems/" + id +"/items/"+menuItem.id);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenuItems(id);
            }catch (error) {
                console.log(error)
            }
        }
    },
});
