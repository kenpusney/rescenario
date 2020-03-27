package net.kimleo.res.model.meta

class MetaInfo {
    MetaType meta

    Map info

    static MetaInfo of(Map yaml) {
        def meta = new MetaInfo()
        meta.meta = MetaType.valueOf(yaml.meta)
        meta.info = yaml
        return meta
    }
}
