import axios from "axios";
import {defineStore} from "pinia";
import {useStorage} from "@vueuse/core";
import {handleError} from "@/utils/errorHandler.js";

export const UseMenuStore = defineStore("menu",{
    state:() => ({
        user: useStorage('user',{}),
        token: useStorage('token',{}),
        menus: [],
        menuItems: [],
        errors: '',
        failure: '',
        isLoading: true,
        success: '',
        current_page: 1,
        current_page_items: 1,
        total_pages: 1,
        total_pages_items: 1,
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
        async fetchMenus(search = '') {
            this.isLoading = true;
            await this.getToken();
            try {
                const response = await axios.get('menu?page=' + this.current_page,{
                    params: {
                        search: search
                    }
                });
                console.log(response.data.data)
                this.total_pages = response.data.data.last_page;
                this.current_page = this.current_page <= this.total_pages ? this.current_page : this.total_pages;
                this.menus = response.data.data;
            } catch (error) {
                console.log(error);
                handleError(error, this);
            }finally {
                this.isLoading = false;
            }
        },
        async fetchMenuItems(search = '', id){
            this.menuItems = [];
            this.isLoading = true;
            await this.getToken();
            try{
                const response = await axios.get('menuItems/'+id+'/items?page=' + this.current_page_items,{
                    params: {
                        search: search
                    }
                });
                console.log(response.data)
                this.total_pages_items = response.data.data.last_page;
                this.current_page_items = this.current_page_items <= this.total_pages_items ? this.current_page_items : this.total_pages_items;
                this.menuItems = response.data.data;
            }catch (error) {
                console.log(error);
                handleError(error, this);
            }finally {
                this.isLoading = false;
            }
        },
        async insertMenu(name, description){
            this.isLoading = true;
            try{
                const response = await axios.post('menu',{
                    name: name,
                    description: description
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenus();
            }catch (error) {
                console.log(error);
                handleError(error, this);
            } finally {
                this.isLoading = false;
            }
        },
        async updateMenu(menu){
            this.isLoading = true;
            try{
                const response = await axios.put("menu/" + menu.id,{
                    name: menu.name,
                    description: menu.description,
                    availability: menu.availability,
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenus();
            }catch (error) {
                console.log(error);
                handleError(error, this);
            } finally {
                this.isLoading = false;
            }
        },
        async destroyMenu(id){
            this.isLoading = true;
            try{
                const response = await axios.delete("menu/" + id);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenus();
            }catch(error) {
                console.log(error);
                handleError(error, this);
            } finally {
                this.isLoading = false;
            }
        },
        async insertMenuItem(id,name, short_description, long_description, recipe, picture, price){
            this.isLoading = true;
            try{
                let formData = new FormData();
                formData.append('picture', picture);
                formData.append('name', name);
                formData.append('short_description', short_description);
                formData.append('long_description', long_description);
                formData.append('recipe', recipe);
                formData.append('price', price);
                const response = await axios.post('menuItems/'+id+'/items',formData,{
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenuItems('',id);
            }catch (error) {
                console.log(error);
                handleError(error, this);
            } finally {
                this.isLoading = false;
            }
        },
        async updateMenuItem(id,menuItem,file){
            this.isLoading = true;
            try{
                let imagePath = null;
                if (file) {
                    const formData = new FormData();
                    formData.append('picture', file);
                    formData.append('name', menuItem.name);
                    const response = await axios.post("menuItems/upload-image", formData,{
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                    imagePath = response.data.image_path;
                }
                const updatedData = {
                    name: menuItem.name,
                    short_description: menuItem.short_description,
                    long_description: menuItem.long_description,
                    recipe: menuItem.recipe,
                    price: menuItem.price,
                    availability: menuItem.availability,
                }
                if (imagePath !== null) {
                    updatedData.picture = imagePath;
                }
                console.log("Updated : "+updatedData);
                const response = await axios.put("menuItems/" + id +"/items/"+menuItem.id,updatedData);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenuItems('',id);
            }catch (error) {
                console.log(error);
                handleError(error, this);
            } finally {
                this.isLoading = false;
            }
        },
        async destroyMenuItem(id,menuItem){
            this.isLoading = true;
            try{
                const response = await axios.delete("menuItems/" + id +"/items/"+menuItem.id);
                console.log(response.data);
                this.success = response.data.message;
                await this.fetchMenuItems('',id);
            }catch (error) {
                console.log(error);
                handleError(error, this);
            } finally {
                this.isLoading = false;
            }
        }
    },
});
