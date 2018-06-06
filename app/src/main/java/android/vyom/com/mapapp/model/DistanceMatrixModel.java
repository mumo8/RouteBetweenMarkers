package android.vyom.com.mapapp.model;

import com.google.gson.Gson;

import java.util.List;

public class DistanceMatrixModel {


    /**
     * destination_addresses : ["1809-1815 Howard St, St. Charles, IL 60174, USA"]
     * origin_addresses : ["1960-1976 Wessel Ct, St. Charles, IL 60174, USA"]
     * rows : [{"elements":[{"distance":{"text":"0.5 mi","value":872},"duration":{"text":"2 mins","value":149},"status":"OK"}]}]
     * status : OK
     */

    private String status;
    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<RowsBean> rows;

    public static DistanceMatrixModel objectFromData(String str) {

        return new Gson().fromJson(str, DistanceMatrixModel.class);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private List<ElementsBean> elements;

        public static RowsBean objectFromData(String str) {

            return new Gson().fromJson(str, RowsBean.class);
        }

        public List<ElementsBean> getElements() {
            return elements;
        }

        public void setElements(List<ElementsBean> elements) {
            this.elements = elements;
        }

        public static class ElementsBean {
            /**
             * distance : {"text":"0.5 mi","value":872}
             * duration : {"text":"2 mins","value":149}
             * status : OK
             */

            private DistanceBean distance;
            private DurationBean duration;
            private String status;

            public static ElementsBean objectFromData(String str) {

                return new Gson().fromJson(str, ElementsBean.class);
            }

            public DistanceBean getDistance() {
                return distance;
            }

            public void setDistance(DistanceBean distance) {
                this.distance = distance;
            }

            public DurationBean getDuration() {
                return duration;
            }

            public void setDuration(DurationBean duration) {
                this.duration = duration;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public static class DistanceBean {
                /**
                 * text : 0.5 mi
                 * value : 872
                 */

                private String text;
                private int value;

                public static DistanceBean objectFromData(String str) {

                    return new Gson().fromJson(str, DistanceBean.class);
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class DurationBean {
                /**
                 * text : 2 mins
                 * value : 149
                 */

                private String text;
                private int value;

                public static DurationBean objectFromData(String str) {

                    return new Gson().fromJson(str, DurationBean.class);
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
        }
    }
}
