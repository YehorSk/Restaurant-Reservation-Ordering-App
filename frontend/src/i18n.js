import { createI18n } from 'vue-i18n'
import en from './locales/en.json'
import uk from './locales/uk.json'
import sk from './locales/sk.json'
import de from './locales/de.json'
import {useStorage} from "@vueuse/core";

function loadLocaleMessages() {
    const locales = [{ en: en }, { uk: uk }, { sk: sk}, { de: de}]
    const messages = {}
    locales.forEach(lang => {
        const key = Object.keys(lang)
        messages[key] = lang[key]
    })
    return messages
}

const lang = useStorage('language','en').value;


export default createI18n({
    locale: lang,
    fallbackLocale: 'en',
    messages: loadLocaleMessages()
})