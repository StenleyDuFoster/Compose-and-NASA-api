package stenleone.nasacompose.mappers.base

interface UiNetworkMapper<Network, UI> {

    fun mapFromEntity(entity: Network): UI

    fun mapToEntity(domainModel: UI): Network? {
        return null
    }

    fun mapFromEntityList(entities: List<Network>): List<UI> {
        return entities.map { e -> mapFromEntity(e) }
    }

    fun mapToEntityList(entities: List<UI>): List<Network?> {
        return entities.map { e -> mapToEntity(e) }
    }

    fun mapFromEntityList(entities: ArrayList<Network>): ArrayList<UI> {
        return ArrayList(entities.map { e -> mapFromEntity(e) })
    }

    fun mapToEntityList(entities: ArrayList<UI>): ArrayList<Network?> {
        return ArrayList(entities.map { e -> mapToEntity(e) })
    }
}